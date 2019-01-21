package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import customer.FoodPlan;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import utils.SerShareConstants;

import java.io.IOException;
import java.util.ArrayList;
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
      ArrayList<FoodPlan> foodPlans = new ArrayList<>(getAgent().getPlans());
      inform.setContentObject(foodPlans);
      inform.setLanguage(SerShareConstants.JAVASERIALIZATION);
      getAgent().send(inform);

      LOGGER.log(Level.INFO, "Send estimate fridge state plan." + foodPlans);
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
