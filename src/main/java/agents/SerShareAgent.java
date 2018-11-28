package agents;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import ontology.peopleExampleOntology.PeopleOntology;

public class SerShareAgent extends Agent {
    private ContentManager manager = (ContentManager) getContentManager();
    private AID[] otherAggents;
    private Codec codec = new SLCodec();

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");
        manager.registerLanguage(codec);
    }

    // Put agent clean-up operations here
    protected void takeDown() {
        // Printout a dismissal message
        System.out.println("Agent " + getAID().getName() + " terminating.");
    }
} // end Agent.java