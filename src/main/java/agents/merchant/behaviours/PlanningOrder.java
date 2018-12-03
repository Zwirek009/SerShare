package agents.merchant.behaviours;

import agents.merchant.MerchantAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Date;

public class PlanningOrder extends CyclicBehaviour {
  private int lastAsking = 0;

  public PlanningOrder(MerchantAgent agent) {
    super(agent);
  }

  public void action() {
    if(getAgent().isOrderTime()) {
      OrderResponse response = getAgent().makeOrder();
      ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
      for (AID m : response.getReceivers()) {
        inform.addReceiver(m);
      }
      try {
        inform.setContentObject(new Date());
      } catch (IOException e) {
        e.printStackTrace();
      }
      getAgent().send(inform);
    } else {
      getAgent().planOrder();
    }
  }

  public MerchantAgent getAgent() {
    return (MerchantAgent)myAgent;
  }
}
