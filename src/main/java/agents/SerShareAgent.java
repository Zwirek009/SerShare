package agents;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
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

    protected AID getService( String service )
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType( service );
        dfd.addServices(sd);
        try
        {
            DFAgentDescription[] result = DFService.search(this, dfd);
            if (result.length>0)
                return result[0].getName() ;
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
        return null;
    }

    protected void register( ServiceDescription sd)
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd );
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
    }

    protected AID [] searchDF( String service )
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType( service );
        dfd.addServices(sd);

        SearchConstraints ALL = new SearchConstraints();
        ALL.setMaxResults(new Long(-1));

        try
        {
            DFAgentDescription[] result = DFService.search(this, dfd, ALL);
            AID[] agents = new AID[result.length];
            for (int i=0; i<result.length; i++)
                agents[i] = result[i].getName();
            return agents;

        }
        catch (FIPAException fe) { fe.printStackTrace(); }

        return null;
    }

    public List<AID> getOtherAgents() {
        return otherAgents;
    }
} // end Agent.java