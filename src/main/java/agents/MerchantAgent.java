package agents;

import customer.FoodPlan;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MerchantAgent extends SerShareAgent {

  private Map<AID, FoodPlan> shoppingList;
  private int lastAsking = 0;

  protected void setup() {
    super.setup();
    this.shoppingList = new HashMap<>();
    addBehaviour(new GetShopingPlans(this));
    addBehaviour(new PlanningOrder(this));
  }

  class PlanningOrder extends CyclicBehaviour {
    PlanningOrder(MerchantAgent agent) {
      super(agent);
    }

    public void action() {
      if(lastAsking > 7) {
        lastAsking = 0;
        System.out.println("Poszły zakupy");
        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
        for (AID m : shoppingList.keySet()) {
          inform.addReceiver(m);
        }
        try {
          inform.setContentObject(new Date());
        } catch (IOException e) {
          e.printStackTrace();
        }
        myAgent.send(inform);
      }
      lastAsking++;
    }
  }

  class GetShopingPlans extends CyclicBehaviour {
    GetShopingPlans(MerchantAgent agent) {
      super(agent);
    }

    public void action() {
      ACLMessage msg = myAgent.receive();
      if(msg != null){
        if(msg.getPerformative()== ACLMessage.INFORM){
          try {
            FoodPlan newPlan = (FoodPlan)msg.getContentObject();
            shoppingList.put(msg.getSender(), newPlan);
          } catch (UnreadableException e) {
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
            reply.setContent("( UnexpectedContent: " + e.getMessage() + ")");
            send(reply);
          }
        }
        else {
          ACLMessage reply = msg.createReply();
          reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
          reply.setContent("( (Unexpected-act "+ACLMessage.getPerformative(msg.getPerformative())+") )");
          send(reply);
        }
      }
      else {
        block();
      }
    }
  }


}
