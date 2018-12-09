package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import customer.FoodPlan;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.SerShareConstants;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import static agents.storekepper.StorekeeperAgent.LOGGER;

public class SendFoodPlanRequest extends MyCFPBehaviour {
  public SendFoodPlanRequest(StorekeeperAgent agent) {
    super(agent, "food-plan");
  }

  @Override
  protected List<AID> getReceivers() {
    return getAgent().getMobiles();
  }

  @Override
  protected void onGettingReply(ACLMessage reply) {
    try {
      FoodPlan newPlan = (FoodPlan) reply.getContentObject();
      getAgent().addFoodPlan(newPlan);

      LOGGER.log(Level.INFO, "Get plan " + newPlan);
    } catch (UnreadableException e) {
      onErrors(reply, "Bad content");
    }
  }

  @Override
  public StorekeeperAgent getAgent() {
    return (StorekeeperAgent)myAgent;
  }
}
