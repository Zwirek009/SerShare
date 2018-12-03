package agents.storekepper;

import agents.SerShareAgent;
import agents.storekepper.behaviours.ActionState;
import agents.storekepper.behaviours.EstimateFridgeStatePlan;
import agents.storekepper.behaviours.SendEstimatedFridgeStatePlan;
import agents.storekepper.behaviours.SendFoodPlanRequest;
import customer.FoodPlan;
import jade.core.AID;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorekeeperAgent extends SerShareAgent {
  private static int resendValue = 60;

  private List<AID> mobiles;
  private AID merchantAgent;
  private List<FoodPlan> plans;
  private Map<String, Double> fridgeStore;
  private ActionState state = ActionState.WAITING;
  private LocalDate lastDate;
  private int resendWhen = 0;

  protected void setup() {
    super.setup();
    this.mobiles = new ArrayList<>();
    this.merchantAgent = new AID("MerchantAgent", true);
    this.plans = new ArrayList<>();
    this.fridgeStore = new HashMap<>();
    this.lastDate = LocalDate.now();
    addBehaviour(new EstimateFridgeStatePlan(this));
  }

  public LocalDate getLastDate() { return lastDate; }

  public ActionState getActionState() { return state; }

  public AID getMerchantAgent() { return merchantAgent; }

  public List<FoodPlan> getPlans() { return plans; }

  public List<AID> getMobiles() {
    return this.mobiles;
  }

  public void addFoodPlan(FoodPlan plan) {
    this.plans.add(plan);
  }

  public boolean hasAllPlans() {
    return plans.size() == mobiles.size();
  }

  public void estimate() {
    if(!this.plans.isEmpty() && !this.mobiles.isEmpty()) {
      this.state = ActionState.RUNNING;
    } else if(this.resendWhen == StorekeeperAgent.resendValue){
      addBehaviour(new SendFoodPlanRequest(this));
      this.resendWhen = 0;
    } else {
      this.resendWhen++;
    }
    if(lastDate.isBefore(LocalDate.now())) {
      addBehaviour(new SendFoodPlanRequest(this));
      addBehaviour(new SendEstimatedFridgeStatePlan(this));
      this.resendWhen = 0;
    }
  }

  private Map<String, Double> getAllProductsDemand() {
    Map<String, Double> all = new HashMap<>();
    return all;
  }

}