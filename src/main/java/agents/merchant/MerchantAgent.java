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
import java.util.stream.Collectors;

import static java.util.Optional.empty;

public class MerchantAgent extends SerShareAgent {

  private Map<AID, FoodPlan> shoppingList;
  private Optional<LocalDate> nextOrder = empty();
  private int lastAsking = 0;

  protected void setup() {
    super.setup();
    this.shoppingList = new HashMap<>();
    addBehaviour(new GettingFoodPlans(this));
    addBehaviour(new PlanningOrder(this));
  }

  public void planOrder() {
    nextOrder = shoppingList.values().stream()
        .flatMap(f -> f.getDates().stream())
        .min(LocalDate::compareTo);
  }

  public boolean isOrderTime() {
    return nextOrder.map(o -> o.isBefore(LocalDate.now())).orElse(false);
  }

  public OrderResponse makeOrder() {
    List<FoodPlanPosition> newOrder = this.shoppingList.values().stream()
        .flatMap(o -> o.getPositionsBeforeDay(LocalDate.now()).stream())
        .collect(Collectors.toList());
    List<AID> customers = this.shoppingList.entrySet().stream()
        .filter(o -> o.getValue().getPositionsBeforeDay(LocalDate.now()).size() > 0)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
    System.out.println("Zakupy: " + newOrder);
    return new OrderResponse(LocalDate.now().plusDays(7), customers);
  }

  public void addFoodPlan(AID client, FoodPlan plan) {
    shoppingList.put(client, plan);
  }

  public Set<AID> getClients() {
    return this.shoppingList.keySet();
  }
}
