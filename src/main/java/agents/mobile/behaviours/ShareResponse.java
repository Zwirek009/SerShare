package agents.mobile.behaviours;

import agents.mobile.MobileAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
;

public class ShareResponse extends CyclicBehaviour {

    public ShareResponse (MobileAgent ma) {super(ma);}

    private String response = "Tak";

    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null && msg.getConversationId().contains("share")) {
            ACLMessage reply = msg.createReply();
            reply.setContent(response);
            reply.setConversationId(msg.getConversationId());
            getAgent().send(reply);
            System.out.println("agent " + myAgent.getName() + " odpowiada " + response + " na " + msg.getConversationId());
        }
        else
            block();

    }


    public MobileAgent getAgent() {
        return (MobileAgent) myAgent;
    }
}
