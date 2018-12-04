package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import db.FridgeStore;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.logging.Level;

public class SendFridgeInternalStateRequest extends CyclicBehaviour {
    public SendFridgeInternalStateRequest(StorekeeperAgent agent) {
        super(agent);
    }

    public void action() {
        StorekeeperAgent.LOGGER.log(Level.INFO, "Waiting for fridge state");

        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            if (msg.getPerformative() == ACLMessage.INFORM) {
                try {
                    FridgeStore fridgeStore = (FridgeStore) msg.getContentObject();
                    getAgent().setFridgeStore(fridgeStore);

                    StorekeeperAgent.LOGGER.log(Level.INFO, "Get fridgeStore " + fridgeStore);
                } catch (UnreadableException e) {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                    reply.setContent("( UnexpectedContent: " + e.getMessage() + ")");
                    getAgent().send(reply);

                    StorekeeperAgent.LOGGER.log(Level.WARNING, "Get understood message " + e.getMessage());
                }
            } else {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                reply.setContent("( (Unexpected-act " + ACLMessage.getPerformative(msg.getPerformative()) + ") )");
                getAgent().send(reply);

                StorekeeperAgent.LOGGER.log(Level.WARNING, "Get unexpected-act " + ACLMessage.getPerformative(msg.getPerformative()));
            }
        } else {
            block();
        }
    }

    public StorekeeperAgent getAgent() {
        return (StorekeeperAgent) myAgent;
    }

}
