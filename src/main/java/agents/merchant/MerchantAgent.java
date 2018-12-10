package agents.merchant;

import agents.SerShareAgent;
import agents.merchant.behaviours.GettingFoodPlans;
import agents.merchant.behaviours.OrderResponse;
import agents.merchant.behaviours.PlanningOrder;
import customer.FoodPlan;
import customer.FoodPlanPosition;
import jade.core.AID;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.stream.Collectors;

import static java.util.Optional.empty;

public class MerchantAgent extends SerShareAgent {

  public static final Logger LOGGER = Logger.getLogger( MerchantAgent.class.getName() );

  private Map<AID, FoodPlan> shoppingList;
  private Optional<LocalDate> nextOrder = empty();

  protected void setup() {
    super.setup();
    LOGGER.setLevel(Level.ALL);
    LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
    this.shoppingList = new HashMap<>();
    addBehaviour(new GettingFoodPlans(this));
    addBehaviour(new PlanningOrder(this));
  }

  public void planOrder() {
    //Todo wmyslec jak planuje zamowienia
    nextOrder = shoppingList.values().stream()
        .flatMap(f -> f.getDates().stream())
        .min(LocalDate::compareTo);
    LOGGER.log(Level.INFO, "PlanOrder: " + nextOrder);
  }

  public boolean isOrderTime() {
    return nextOrder.map(o -> o.isBefore(LocalDate.now())).orElse(false);
  }

  public OrderResponse makeOrder() {
    List<FoodPlanPosition> newOrder = this.shoppingList.values().stream()
        .flatMap(o -> o.getPositionsBeforeDay(LocalDate.now().plusDays(1)).stream())
        .collect(Collectors.toList());
    List<AID> customers = this.shoppingList.entrySet().stream()
        .filter(o -> o.getValue().getPositionsBeforeDay(LocalDate.now().plusDays(1)).size() > 0)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
    customers.forEach(c -> {
      FoodPlan plan = this.shoppingList.get(c);
      plan.deletePositionsToDay(LocalDate.now().plusDays(1));
      this.shoppingList.replace(c, plan);
    });
    LOGGER.log(Level.INFO, "Make order: " + newOrder + " : ");
    return new OrderResponse(LocalDate.now().plusDays(7), customers);
  }

  public void addFoodPlan(AID client, FoodPlan plan) {
    shoppingList.put(client, plan);
  }

  public Set<AID> getClients() {
    return this.shoppingList.keySet();
  }
}
