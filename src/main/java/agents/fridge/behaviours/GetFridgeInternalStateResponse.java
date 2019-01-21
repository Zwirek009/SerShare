package agents.fridge.behaviours;

import agents.fridge.FridgeAgent;
import agents.mobile.MobileAgent;
import db.FridgeStore;
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
        MobileAgent.LOGGER.log(Level.INFO, "Waiting for fridge state request.");
        ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId("fridge-state"));
        if (msg != null) {
            LOGGER.log(Level.INFO, "Get fridge state request " + msg);

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM_REF);
            reply.setLanguage(SerShareConstants.JAVASERIALIZATION);
            FridgeStore fs = ((FridgeAgent)myAgent).getFridgeStore();
            try {
                reply.setContentObject(fs);
            } catch (IOException e) {
                e.printStackTrace();
            }
            getAgent().send(reply);
            LOGGER.log(Level.INFO, "Send fridge state response " + fs);
        }
        block();

    }

    public FridgeAgent getAgent() {
        return (FridgeAgent) myAgent;
    }

}
