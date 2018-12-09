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

public class StorekeeperAgent extends SerShareAgent {
  private static int resendValue = 1;

  public static final Logger LOGGER = Logger.getLogger(StorekeeperAgent.class.getName());

  private List<AID> mobiles;
  private List<AID> fridges;
  private AID merchantAgent;
  private List<FoodPlan> plans;
  private FridgeStore fridgeStore;
  private ActionState state = ActionState.WAITING;
  private LocalDate lastDate;
  private int resendWhen = 0;
  private Behaviour sendFoodPlanReguest;

  protected void setup() {
    super.setup();
    LOGGER.setLevel(Level.ALL);
    LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
    this.mobiles = new ArrayList<>();
    this.merchantAgent = new AID("merchantAgent0", AID.ISLOCALNAME);
    this.plans = new ArrayList<>();
    this.fridgeStore = new FridgeStore();
    this.lastDate = LocalDate.now();
    this.fridges = Collections.singletonList(new AID("FridgeAgent", AID.ISLOCALNAME));
    this.sendFoodPlanReguest = new SendFoodPlanRequest(this);
    addBehaviour(new GetMobileAid(this));
    addBehaviour(new EstimateFridgeStatePlan(this));
    addBehaviour(this.sendFoodPlanReguest);
    addBehaviour(new SendFridgeStateRequest(this));
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

  public void addNewMobile(AID newMobile) { if(!this.mobiles.contains(newMobile)) this.mobiles.add(newMobile);}

  public boolean hasAllFridgeStates() {
    return true;
  }

  public void estimate() {
    //TODO
    if(!this.plans.isEmpty() && !this.mobiles.isEmpty() && this.state != ActionState.RUNNING) {
      this.state = ActionState.RUNNING;
    }
    if(this.resendWhen == StorekeeperAgent.resendValue) {
      addBehaviour(new SendEstimatedFridgeStatePlan(this));
      this.sendFoodPlanReguest.reset();
      this.resendWhen = 0;
    }
    this.resendWhen++;
  }

  private Map<String, Double> getAllProductsDemand() {
    Map<String, Double> all = new HashMap<>();
    return all;
  }

}