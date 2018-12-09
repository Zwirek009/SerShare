package agents.fridge.behaviours;

import agents.fridge.FridgeAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.SerShareConstants;

import java.io.IOException;
import java.util.logging.Level;

import static agents.fridge.FridgeAgent.LOGGER;

public class GetFridgeInternalStateResponse extends CyclicBehaviour {

    public GetFridgeInternalStateResponse(FridgeAgent fridgeAgent, DataStore ds) {
        super(fridgeAgent);
        setDataStore(ds);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId("fridge-state"));
        LOGGER.log(Level.INFO, "Get fridge state request " + msg);
        if (msg != null) {
            LOGGER.log(Level.INFO, "Get fridge state request " + msg);

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setLanguage(SerShareConstants.JAVASERIALIZATION);
            try {
                reply.setContentObject(getAgent().getFridgeStore());
            } catch (IOException e) {
                e.printStackTrace();
            }
            getAgent().send(reply);
            LOGGER.log(Level.INFO, "Send fridge state response " + reply.getContent());
        }
        block();

    }

    public FridgeAgent getAgent() {
        return (FridgeAgent) myAgent;
    }

}
