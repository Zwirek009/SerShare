package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import customer.FoodPlan;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.SerShareConstants;

import java.util.ArrayList;
import java.util.logging.Level;

import static agents.storekepper.StorekeeperAgent.LOGGER;

public class SendFoodPlanRequest extends Behaviour {
      private MessageTemplate mt; // The template to receive replies
      private int step = 0;

      public SendFoodPlanRequest(StorekeeperAgent agent) {
        super(agent);
  }

  public void action() {
    switch (step) {
      case 0:
        // wysłanie zapytania do wszystkich
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        for (AID m : getAgent().getMobiles()) {
          cfp.addReceiver(m);
        }
        cfp.setConversationId("food-plan");
        cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
        cfp.setLanguage(SerShareConstants.JAVASERIALIZATION);
        myAgent.send(cfp);
        // Prepare the template to get proposals
        mt = MessageTemplate.and(MessageTemplate.MatchConversationId("food-plan"),
            MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
        step = 2;
        LOGGER.log(Level.INFO, "Send food plan request");
        break;
      case 1:
        // pobranie wszystkich odpowiedzi
        ACLMessage reply = myAgent.receive(mt);
        try {
          if (reply != null) {
            // Reply received
            if (reply.getPerformative() == ACLMessage.INFORM && reply.getLanguage().equals(SerShareConstants.JAVASERIALIZATION)) {
              FoodPlan p = (FoodPlan) reply.getContentObject();
              getAgent().addFoodPlan(p);
            } else {
              LOGGER.log(Level.WARNING, "Unexpected response");
            }
            LOGGER.log(Level.INFO, "Get food plan");
            if (getAgent().hasAllPlans()) {
              LOGGER.log(Level.INFO, "Get all food plan");
              step = 2;
            }
          } else {
            block();
          }
        } catch (UnreadableException e3) {
          System.err.println(this.myAgent.getLocalName() + " catched exception " + e3.getMessage());
        }
        break;
    }
  }

  public StorekeeperAgent getAgent() {
    return (StorekeeperAgent)myAgent;
  }

  public boolean done() {
    return step == 2;
  }
}
