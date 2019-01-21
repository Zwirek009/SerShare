package agents.storekepper;

import agents.SerShareAgent;
import agents.storekepper.behaviours.*;
import customer.FoodPlan;
import customer.FoodPlanPosition;
import db.FoodProduct;
import db.FridgeStore;
import jade.core.AID;
import jade.core.behaviours.Behaviour;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.stream.Collectors;

import static utils.SerShareConstants.FRIDGE_AGENT_NAME;
import static utils.SerShareConstants.MERCHANT_AGENT_NAME;
import static utils.SerShareConstants.MOBILE_AGENT_NAME;

public class StorekeeperAgent extends SerShareAgent {
  private static int resendValue = 7;

  public static final Logger LOGGER = Logger.getLogger(StorekeeperAgent.class.getName());

  private List<FoodPlan> plans;
  private FridgeStore fridgeStore;
  private ActionState state = ActionState.WAITING;
  private LocalDate lastDate;
  private int resendWhen = 0;
  private Behaviour sendFoodPlanReguest;
  private Behaviour sendFridgeStateReguest;

  private int lastFridgeState = 7;

  protected void setup() {
    super.setup();
    LOGGER.setLevel(Level.ALL);
    LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));

    this.plans = new ArrayList<>();
    this.fridgeStore = new FridgeStore();
    this.lastDate = LocalDate.now();

    this.sendFoodPlanReguest = new SendFoodPlanRequest(this);
    this.sendFridgeStateReguest = new SendFridgeStateRequest(this);

    addBehaviour(new EstimateFridgeStatePlan(this));

    addBehaviour(this.sendFoodPlanReguest);
    addBehaviour(this.sendFridgeStateReguest);
  }

  public LocalDate getLastDate() { return lastDate; }

  public ActionState getActionState() { return state; }

  public AID getMerchantAgent() { return this.getService(MERCHANT_AGENT_NAME); }

  public List<FoodPlan> getPlans() { return plans; }

  public List<AID> getMobiles() {
    return Arrays.asList(this.searchDF(MOBILE_AGENT_NAME));
  }

  public List<AID> getFridges() {
    return Arrays.asList(this.searchDF(FRIDGE_AGENT_NAME));
  }

  public void setFridgeStore(FridgeStore fridgeStore) {
    this.fridgeStore = fridgeStore;
  }

  public void addFoodPlan(FoodPlan plan) {
    this.plans.add(plan);
  }

  public boolean hasAllFridgeStates() {
    return true;
  }

  public void estimate() {
    if(!this.plans.isEmpty() && this.state != ActionState.RUNNING) {
      this.state = ActionState.RUNNING;
    }
    if(this.lastFridgeState == 0) {
      this.sendFridgeStateReguest.reset();
      this.lastFridgeState = 7;
    }
    if(this.resendWhen == StorekeeperAgent.resendValue) {
      addBehaviour(new SendEstimatedFridgeStatePlan(this));
      this.sendFoodPlanReguest.reset();
      this.resendWhen = 0;
    }
    if(this.plans.isEmpty()) {
      this.sendFoodPlanReguest.reset();
    }
    this.updateFoodPlans();
    this.resendWhen++;
    this.lastFridgeState--;
  }

  private void updateFoodPlans() {
    try {
      this.plans = this.plans.stream().filter(p -> !p.isEmpty()).collect(Collectors.toList());
      this.plans.forEach(p -> {
        List<Boolean> foodPlanPositionList = p.getPositions(LocalDate.now()).stream().map(f -> {
          Boolean isInFridge =
              this.fridgeStore.getProducts().stream().
                  anyMatch(fridgeP -> fridgeP.getName().equals(f.getProduct()) && fridgeP.getQuantity() <= f.getQuantity());

          if (isInFridge) {
            p.deletePosition(LocalDate.now(), f);
          }
          return isInFridge;
        }).collect(Collectors.toList());

        if (foodPlanPositionList.stream().allMatch(a -> a)) {
          p.deletePositions(LocalDate.now());
        }
        p.deletePositionsToDay(LocalDate.now().minusDays(1));
      });
    } catch (Exception e) {
      this.plans.forEach(p -> p.deletePositionsToDay(LocalDate.now()));
    }
  }

  private Map<String, Double> getAllProductsDemand() {
    Map<String, Double> all = new HashMap<>();
    return all;
  }

}