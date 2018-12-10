package agents;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import ontology.peopleExampleOntology.PeopleOntology;

import java.util.ArrayList;
import java.util.List;

public class SerShareAgent extends Agent {
    private ContentManager manager = (ContentManager) getContentManager();

    private List<AID> otherAgents;
    private Codec codec = new SLCodec();

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");
        manager.registerLanguage(codec);

        otherAgents = new ArrayList<>();
        String[] args = (String[])getArguments();
        if (args == null || args.length == 0)
            return;
        for (String arg : args)
            otherAgents.add(new AID(arg, false));
    }

    public void sendStringReply(ACLMessage msg, int type, String content) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(type);
        reply.setContent(content);
        send(reply);
    }

    // Put agent clean-up operations here
    protected void takeDown() {
        // Printout a dismissal message
        System.out.println("Agent " + getAID().getName() + " terminating.");
    }

    public List<AID> getOtherAgents() {
        return otherAgents;
    }
} // end Agent.java