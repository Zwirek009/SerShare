package agents.merchant.behaviours;

import agents.merchant.MerchantAgent;
import customer.FoodPlan;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.logging.Level;

public class GettingFoodPlans extends CyclicBehaviour {
  public GettingFoodPlans(MerchantAgent agent) {
    super(agent);
  }

  public void action() {
    MerchantAgent.LOGGER.log(Level.INFO, "Waiting for plan");

    ACLMessage msg = myAgent.receive();
    if(msg != null){
      if(msg.getPerformative()== ACLMessage.INFORM){
        try {
          FoodPlan newPlan = (FoodPlan)msg.getContentObject();
          getAgent().addFoodPlan(msg.getSender(), newPlan);

          MerchantAgent.LOGGER.log(Level.INFO, "Get plan " + newPlan);
        } catch (UnreadableException e) {
          ACLMessage reply = msg.createReply();
          reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
          reply.setContent("( UnexpectedContent: " + e.getMessage() + ")");
          getAgent().send(reply);

          MerchantAgent.LOGGER.log(Level.WARNING, "Get understood message " + e.getMessage());
        }
      }
      else {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        reply.setContent("( (Unexpected-act "+ACLMessage.getPerformative(msg.getPerformative())+") )");
        getAgent().send(reply);

        MerchantAgent.LOGGER.log(Level.WARNING, "Get unexpected-act " + ACLMessage.getPerformative(msg.getPerformative()));
      }
    }
    else {
      block();
    }
  }

  public MerchantAgent getAgent() {
    return (MerchantAgent)myAgent;
  }
}
