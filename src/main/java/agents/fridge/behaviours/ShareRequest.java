package agents.fridge.behaviours;

import agents.fridge.FridgeAgent;
import db.FoodProduct;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.SerShareConstants;

import java.io.IOException;


public class ShareRequest extends Behaviour {

    private String shareResponse;
    private MessageTemplate mt;
    private FoodProduct product;
    private int step = 0;
    public ShareRequest (FridgeAgent fa, FoodProduct prod) {
        super(fa);
        product = prod;
    }

    public void action() {
        switch (step) {
            case 0:
                // wysłanie zapytania do wszystkich
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for (AID m : getAgent().getMobiles()) {
                    cfp.addReceiver(m);
                }
                try {
                    cfp.setContentObject(product);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cfp.setConversationId("share");
                cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
                cfp.setLanguage(SerShareConstants.JAVASERIALIZATION);
                myAgent.send(cfp);
                System.out.println("send request " + product.toString() + " to " + getAgent().getMobiles() + "  ");
                step = 1;
                break;
            case 1:
                // pobranie wszystkich odpowiedzi
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null && reply.getConversationId().equals("share" + product.toString())) {
                    shareResponse = reply.getContent();
                    System.out.println("odp: " + shareResponse + " na " + product.toString());
                    step = 2;
                }
                else
                    block();
                break;

        }
    }

    public boolean done() {
        return step == 2;
    }

    public FridgeAgent getAgent() {
        return (FridgeAgent)myAgent;
    }
}
