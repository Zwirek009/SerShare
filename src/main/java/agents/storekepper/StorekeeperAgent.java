package agents.storekepper;

import agents.SerShareAgent;
import agents.storekepper.behaviours.*;
import customer.FoodPlan;
import db.FridgeStore;
import jade.core.AID;
import jade.core.behaviours.Behaviour;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

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
    //TODO
    if(!this.plans.isEmpty() && this.state != ActionState.RUNNING) {
      this.state = ActionState.RUNNING;
    }
    if(this.resendWhen == StorekeeperAgent.resendValue) {
      addBehaviour(new SendEstimatedFridgeStatePlan(this));
      this.sendFoodPlanReguest.reset();
      this.sendFridgeStateReguest.reset();
      this.resendWhen = 0;
    }
    this.resendWhen++;
  }

  private Map<String, Double> getAllProductsDemand() {
    Map<String, Double> all = new HashMap<>();
    return all;
  }

}