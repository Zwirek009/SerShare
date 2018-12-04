package agents.merchant.behaviours;

import agents.merchant.MerchantAgent;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class PlanningOrder extends TickerBehaviour {
  public PlanningOrder(MerchantAgent agent) {
    super(agent, TimeUnit.MINUTES.toMillis(1));
  }

  @Override
  protected void onTick() {
    MerchantAgent.LOGGER.log(Level.INFO, "Tick");
    if(getAgent().isOrderTime()) {
      OrderResponse response = getAgent().makeOrder();
      ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
      for (AID m : response.getReceivers()) {
        inform.addReceiver(m);
      }
      MerchantAgent.LOGGER.log(Level.INFO, "Send messages to " + response.getReceivers());
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
