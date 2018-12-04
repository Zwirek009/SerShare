package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.logging.Level;

import static agents.storekepper.StorekeeperAgent.LOGGER;

public class SendEstimatedFridgeStatePlan extends Behaviour {
  public SendEstimatedFridgeStatePlan(StorekeeperAgent agent) {
    super(agent);
  }

  public void action() {
    try {
      ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
      inform.addReceiver(getAgent().getMerchantAgent());
      inform.setContentObject(getAgent().getPlans().toArray());
      getAgent().send(inform);

      LOGGER.log(Level.INFO, "Send estimate fridge state plan request");
    } catch (IOException e) {
      LOGGER.log(Level.INFO, "Error when try to sen fridge plans " + e.getMessage());
    }
  }

  public StorekeeperAgent getAgent() {
    return (StorekeeperAgent)myAgent;
  }

  public boolean done() {
    return true;
  }
}
