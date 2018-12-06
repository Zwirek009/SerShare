package agents.mobile.behaviours;

import agents.mobile.MobileAgent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
;

public class ShareResponse extends Behaviour {

    public ShareResponse (MobileAgent ma) {super(ma);}

    private String response = "Tak";
    private Boolean end = false;

    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {

            ACLMessage reply = msg.createReply();
            reply.setContent(response);

            getAgent().send(reply);
            end = true;
        }
        block();
    }

    public boolean done() {
        return end;
    }

    public MobileAgent getAgent() {
        return (MobileAgent) myAgent;
    }
}
