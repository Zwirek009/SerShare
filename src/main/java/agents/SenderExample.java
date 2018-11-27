package agents;

import jade.content.ContentManager;
import jade.content.*;
import jade.content.abs.*;
import jade.content.onto.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import ontology.peopleExampleOntology.Address;
import ontology.peopleExampleOntology.FatherOf;
import ontology.peopleExampleOntology.Man;
import ontology.peopleExampleOntology.PeopleOntology;

public class SenderExample extends Agent {
  // We handle contents
  private ContentManager manager  = (ContentManager)getContentManager();
  // This agent speaks the SL language
  private Codec codec    = new SLCodec();
  // This agent complies with the People ontology
  private Ontology ontology = PeopleOntology.getInstance();

  class SenderBehaviour extends SimpleBehaviour {
    private boolean finished = false;

    public SenderBehaviour(Agent a) { super(a); }

    public boolean done() { return finished; }

    public void action() {
      try {
        // Preparing the first message
        System.out.println( "[" + getLocalName() + "] Creating inform message with content fatherOf(man :name John :address London, [man :name Bill :address Paris])");

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        AID receiver = new AID("receiver", AID.ISLOCALNAME);

        msg.setSender(getAID());
        msg.addReceiver(receiver);
        msg.setLanguage(codec.getName());
        msg.setOntology(ontology.getName());

        // The message informs that:
        // fatherOf(man :name "John" :address "London", [man :name "Bill" :address "Paris"])

        Man john = new Man();
        Man bill = new Man();
        john.setName("John");
        bill.setName("Bill");

        Address johnAddress = new Address();
        johnAddress.setCity("London");
        john.setAddress(johnAddress);

        Address billAddress = new Address();
        billAddress.setCity("Paris");
        bill.setAddress(billAddress);

        FatherOf fatherOf = new FatherOf();
        fatherOf.setFather(john);

        List children = new ArrayList();
        children.add(bill);

        fatherOf.setChildren(children);

        // Fill the content of the message
        manager.fillContent(msg, fatherOf);

        // Send the message
        System.out.println( "[" + getLocalName() + "] Sending the message...");
        send(msg);

        // Now ask the proposition back.
        // Use a query-ref with the following content:
        // iota ?x fatherOf(?x, [man :name "Bill" :address "Paris"])
        System.out.println( "[" + getLocalName() + "] Creating query-ref message with content iota ?x fatherOf(?x, [man :name Bill :address Paris])");
        msg.setPerformative(ACLMessage.QUERY_REF);

        // Create an abstract descriptor from scratch
        AbsConcept absBill = new AbsConcept(PeopleOntology.MAN);
        absBill.set(PeopleOntology.NAME, "Bill");

        // Create an abstract descriptor from a concrete object
        AbsConcept absBillAddress = (AbsConcept)ontology.fromObject(billAddress);
        absBill.set(PeopleOntology.ADDRESS, absBillAddress);

        AbsAggregate absChildren = new AbsAggregate(BasicOntology.SEQUENCE);
        absChildren.add(absBill);

        AbsVariable absX = new AbsVariable("x", PeopleOntology.MAN);

        AbsPredicate absFatherOf = new AbsPredicate(PeopleOntology.FATHER_OF);
        absFatherOf.set(PeopleOntology.FATHER, absX);
        absFatherOf.set(PeopleOntology.CHILDREN, absChildren);

        AbsIRE absIRE = new AbsIRE(SLVocabulary.IOTA);
        absIRE.setVariable(absX);
        absIRE.setProposition(absFatherOf);

        // Fill the content of the message
        manager.fillContent(msg, absIRE);

        // Send the message
        System.out.println( "[" + getLocalName() + "] Sending the message...");
        send(msg);
      } catch(Exception e) { e.printStackTrace(); }

      finished = true;
    }
  }

  protected void setup() {
    manager.registerLanguage(codec);
    manager.registerOntology(ontology);

    addBehaviour(new SenderBehaviour(this));
  }
}
