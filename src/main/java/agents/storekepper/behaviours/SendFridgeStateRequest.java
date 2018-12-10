package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import customer.FoodPlan;
import db.FridgeStore;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.SerShareConstants;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import static agents.storekepper.StorekeeperAgent.LOGGER;

public class SendFridgeStateRequest extends MyCFPBehaviour {
  public SendFridgeStateRequest(StorekeeperAgent agent) {
    super(agent, "fridge-state");
  }

  @Override
  protected List<AID> getReceivers() {
    return getAgent().getFridges();
  }

  @Override
  protected void onGettingReply(ACLMessage reply) {
    try {
      //get fridge state
      FridgeStore store = (FridgeStore) reply.getContentObject();
      getAgent().setFridgeStore(store);

      LOGGER.log(Level.INFO, "Get fridge state " + store);
    } catch (UnreadableException | ClassCastException e) {
      onErrors(reply, "Bad content");
    }
  }

  @Override
  public StorekeeperAgent getAgent() {
    return (StorekeeperAgent)myAgent;
  }
}
