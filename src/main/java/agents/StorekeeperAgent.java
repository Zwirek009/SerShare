package agents;

import behaviours.SendFoodPlanRequest;
import customer.FoodPlan;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorekeeperAgent extends SerShareAgent {

  private enum State {WAITING, RUNNING}

  private AID[] mobiles;
  private AID orderManager;
  private List<FoodPlan> plans;
  private Map<String, Double> fridgeStore;
  private State state = State.WAITING;
  private int lastAsking = 0;

  protected void setup() {
    super.setup();
    this.mobiles = new AID[0];
    this.plans = new ArrayList<>();
    this.fridgeStore = new HashMap<>();
    addBehaviour(new EstimateFridgeStatePlan(this));
  }

  class EstimateFridgeStatePlan extends CyclicBehaviour {
    EstimateFridgeStatePlan(StorekeeperAgent agent) {
      super(agent);
    }

    public void action() {
      if(lastAsking > 7)
        lastAsking = 0;
      switch (state) {
        case WAITING:
          addBehaviour(new SendFoodPlanRequest((StorekeeperAgent)this.myAgent));
         //addBehaviour(new GetFridgeInternalStateRequest((StorekeeperAgent)this.myAgent));
          state = State.RUNNING;
        case RUNNING:
            if(lastAsking == 7){
              state = State.WAITING;
              ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
              inform.addReceiver(orderManager);
              try {
                inform.setContentObject(plans.toArray());
              } catch (IOException e) {
                e.printStackTrace();
              }
              myAgent.send(inform);
            } else {
              estimate();
            }
        break;
      }
      lastAsking++;
    }
  }

  public AID[] getMobiles() {
    return this.mobiles;
  }

  public void updateFoodPlans(List<FoodPlan> plans) {
    this.plans = plans;
  }

  private void estimate() {
    System.out.println(getAllProductsDemand());
  }

  private Map<String, Double> getAllProductsDemand() {
    Map<String, Double> all = new HashMap<>();
    return all;
  }

}