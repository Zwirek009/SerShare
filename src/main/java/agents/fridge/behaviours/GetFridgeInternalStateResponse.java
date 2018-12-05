package agents.fridge.behaviours;

import agents.fridge.FridgeAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class GetFridgeInternalStateResponse extends Behaviour {

    public GetFridgeInternalStateResponse(FridgeAgent fridgeAgent, DataStore ds) {
        super(fridgeAgent);
        setDataStore(ds);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            System.out.println(" - " + myAgent.getLocalName() + " <- " + msg.getContent());

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(getAgent().getFridgeStore());
            } catch (IOException e) {
                e.printStackTrace();
            }
            getAgent().send(reply);
        }
        block();

    }

    @Override
    public boolean done() {
        return false;
    }

    public FridgeAgent getAgent() {
        return (FridgeAgent) myAgent;
    }

}
