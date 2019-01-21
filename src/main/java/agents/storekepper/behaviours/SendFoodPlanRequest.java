package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import customer.FoodPlan;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.List;
import java.util.logging.Level;

public class SendFoodPlanRequest extends MyQueryBehaviour {
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
    } catch (UnreadableException | ClassCastException e) {
      onErrors(reply, "Bad content:" + reply + e.toString());
    }
  }

  @Override
  public StorekeeperAgent getAgent() {
    return (StorekeeperAgent)myAgent;
  }
}
