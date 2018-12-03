package behaviours;

import agents.StorekeeperAgent;
import customer.FoodPlan;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.SerShareConstants;

import java.util.ArrayList;
import java.util.List;

public class SendFoodPlanRequest extends Behaviour {
      private AID[] mobiles;
      private List<FoodPlan> plans;
      private MessageTemplate mt; // The template to receive replies
      private int step = 0;

      public SendFoodPlanRequest(StorekeeperAgent agent) {
        super(agent);
        this.mobiles = agent.getMobiles();
        this.plans = new ArrayList<>();
  }

  public void action() {
    switch (step) {
      case 0:
        // wysłanie zapytania do wszystkich
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        for (AID m : mobiles) {
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
        System.out.println("ASK");
        break;
      case 1:
        // pobranie wszystkich odpowiedzi
        ACLMessage reply = myAgent.receive(mt);
        try {
          if (reply != null) {
            // Reply received
            if (reply.getPerformative() == ACLMessage.INFORM && reply.getLanguage().equals(SerShareConstants.JAVASERIALIZATION)) {
              FoodPlan p = (FoodPlan) reply.getContentObject();
              this.plans.add(p);
            } else {
              System.out.println("Unexpected response");
            }
            if (this.plans.size() >= mobiles.length) {
              ((StorekeeperAgent)this.myAgent).updateFoodPlans(this.plans);
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

  public boolean done() {
    return step == 2;
  }
}
