package agents.merchant.behaviours;

import agents.merchant.MerchantAgent;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static agents.merchant.MerchantAgent.LOGGER;

public class PlanningOrder extends TickerBehaviour {
  public PlanningOrder(MerchantAgent agent) {
    super(agent, TimeUnit.SECONDS.toMillis(32));
  }

  @Override
  protected void onTick() {
    LOGGER.log(Level.INFO, "Tick");
    if(getAgent().isOrderTime()) {
      OrderResponse response = getAgent().makeOrder();
      ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
      for (AID m : response.getReceivers()) {
        inform.addReceiver(m);
      }
      try {
        inform.setContentObject(new Date());
        getAgent().send(inform);
        LOGGER.log(Level.INFO, "Send messages to " + response.getReceivers());
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, "Error when try send messages " + e.getMessage());
      }
    } else {
      getAgent().planOrder();
    }
  }

  public MerchantAgent getAgent() {
    return (MerchantAgent)myAgent;
  }
}
