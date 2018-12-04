package agents.storekepper;

import agents.SerShareAgent;
import agents.storekepper.behaviours.*;
import customer.FoodPlan;
import db.FridgeStore;
import jade.core.AID;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class StorekeeperAgent extends SerShareAgent {
  private static int resendValue = 2;

    public static final Logger LOGGER = Logger.getLogger(StorekeeperAgent.class.getName());

  private List<AID> mobiles;
  private List<AID> fridges;
  private AID merchantAgent;
  private List<FoodPlan> plans;
  private FridgeStore fridgeStore;
  private ActionState state = ActionState.WAITING;
  private LocalDate lastDate;
  private int resendWhen = 0;

  protected void setup() {
    super.setup();
    LOGGER.setLevel(Level.ALL);
    LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
    this.mobiles = new ArrayList<>();
    this.merchantAgent = new AID("MerchantAgent", true);
    this.plans = new ArrayList<>();
    this.fridgeStore = new FridgeStore();
    this.lastDate = LocalDate.now();
    getFridgeStatusesAndFoodPlans();
    addBehaviour(new EstimateFridgeStatePlan(this));
  }

  public LocalDate getLastDate() { return lastDate; }

  public ActionState getActionState() { return state; }

  public AID getMerchantAgent() { return merchantAgent; }

  public List<FoodPlan> getPlans() { return plans; }

  public List<AID> getMobiles() {
    return this.mobiles;
  }

  public List<AID> getFridges() {
    return this.fridges;
  }

  public void setFridgeStore(FridgeStore fridgeStore) {
    this.fridgeStore = fridgeStore;
  }

  public void addFoodPlan(FoodPlan plan) {
    this.plans.add(plan);
  }

  public boolean hasAllPlans() {
    return plans.size() == mobiles.size();
  }

  public boolean hasAllFridgeStates() {
    return true;
  }

  public void estimate() {
    if(!this.plans.isEmpty() && !this.mobiles.isEmpty()) {
      this.state = ActionState.RUNNING;
    } else if(this.resendWhen == StorekeeperAgent.resendValue){
      getFridgeStatusesAndFoodPlans();

      LOGGER.log(Level.INFO, "Resend food plan request");
      this.resendWhen = 0;
    } else {
      this.resendWhen++;
    }
    if(lastDate.isBefore(LocalDate.now())) {
      getFridgeStatusesAndFoodPlans();
      addBehaviour(new SendEstimatedFridgeStatePlan(this));
      this.resendWhen = 0;
    }
  }

  private void getFridgeStatusesAndFoodPlans() {
    addBehaviour(new SendFoodPlanRequest(this));
    addBehaviour(new SendFridgeStateRequest(this));
  }

  private Map<String, Double> getAllProductsDemand() {
    Map<String, Double> all = new HashMap<>();
    return all;
  }

}