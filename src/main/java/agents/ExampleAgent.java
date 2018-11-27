package agents;

import behaviours.MyExampleBehaviour;
import jade.content.*;
import jade.content.abs.*;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.*;
import jade.content.lang.*;
import jade.content.lang.leap.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import ontology.peopleExampleOntology.FatherOf;
import ontology.peopleExampleOntology.PeopleOntology;


public class ExampleAgent extends Agent {
  private ContentManager manager = (ContentManager) getContentManager();
  private AID[] otherAggents;
  private Codec codec = new SLCodec();
  private Ontology ontology = PeopleOntology.getInstance();
  private FatherOf proposition = null;

  class ReceiverBehaviour extends SimpleBehaviour {
    private boolean finished = false;

    public ReceiverBehaviour(Agent a) {
      super(a);
    }

    public boolean done() {
      return finished;
    }

    public void action() {
      for (int c = 0; c < 2; c++) {
        try {
          System.out.println("[" + getLocalName() + "] Waiting for a message...");

          ACLMessage msg = blockingReceive();

          if (msg != null) {
            switch (msg.getPerformative()) {
              case ACLMessage.INFORM:
                ContentElement p = manager.extractContent(msg);
                if (p instanceof FatherOf) {
                  proposition = (FatherOf) p;
                  System.out.println("[" + getLocalName() + "] Receiver inform message: information stored.");
                  break;
                }
              case ACLMessage.QUERY_REF:
                AbsContentElement abs = manager.extractAbsContent(msg);
                if (abs instanceof AbsIRE) {
                  AbsIRE ire = (AbsIRE) abs;

                  ACLMessage reply = msg.createReply();
                  reply.setPerformative(ACLMessage.INFORM);

                  AID sender = new AID("sender", AID.ISLOCALNAME);

                  reply.setSender(getAID());
                  reply.addReceiver(sender);
                  reply.setLanguage(codec.getName());
                  reply.setOntology(ontology.getName());

                  AbsConcept absFather = (AbsConcept) ontology.fromObject(proposition.getFather());

                  AbsPredicate absEquals = new AbsPredicate(BasicOntology.EQUALS);
                  absEquals.set(BasicOntology.EQUALS_LEFT, ire);
                  absEquals.set(BasicOntology.EQUALS_RIGHT, absFather);

                  manager.fillContent(reply, absEquals);

                  send(reply);

                  System.out.println("[" + getLocalName() + "] Received query-ref message: reply sent:");
                  System.out.println(absEquals);
                  break;
                }
              default:
                System.out.println("[" + getLocalName() + "] Malformed message.");
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      finished = true;
    }
  }

  protected void setup() {
    System.out.println("Agent " + getLocalName() + " started.");
    manager.registerLanguage(codec);
    manager.registerOntology(ontology);

    // Add the CyclicBehaviour
    addBehaviour(new CyclicBehaviour(this) {
      public void action() {
        System.out.println("Cycling");
      }
    });

    // Add the ReceiverBehaviour
    addBehaviour(new ReceiverBehaviour(this));

    // Add the generic behaviour
    addBehaviour(new MyExampleBehaviour());
  }

  // Put agent clean-up operations here
  protected void takeDown() {
    // Printout a dismissal message
    System.out.println("Agent " + getAID().getName() + " terminating.");
  }
} // end Agent.java
