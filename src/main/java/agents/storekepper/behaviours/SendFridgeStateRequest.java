package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import db.FridgeStore;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.List;
import java.util.logging.Level;

public class SendFridgeStateRequest extends MyQueryBehaviour {
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
      onErrors(reply, "Bad content: " + reply + e.toString());
    }
  }

  @Override
  public StorekeeperAgent getAgent() {
    return (StorekeeperAgent)myAgent;
  }
}
