package agents.merchant.behaviours;

import agents.merchant.MerchantAgent;
import customer.FoodPlan;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import utils.SerShareConstants;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;

import static agents.merchant.MerchantAgent.LOGGER;

public class GettingFoodPlans extends CyclicBehaviour {
  public GettingFoodPlans(MerchantAgent agent) {
    super(agent);
  }

  public void action() {
    LOGGER.log(Level.INFO, "Waiting for plan");

    ACLMessage msg = myAgent.receive();

    try {
      if (msg != null) {
        Optional<String> errors = validateMessage(msg);
        if(msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {
          LOGGER.log(Level.INFO, "Not understood");
        } else if (errors.isPresent()) {
          getAgent().sendStringReply(msg, ACLMessage.NOT_UNDERSTOOD, errors.get());

          LOGGER.log(Level.WARNING, errors.get());
        } else {
          ArrayList<FoodPlan> newPlans = (ArrayList<FoodPlan>) msg.getContentObject();
          newPlans.forEach(p -> getAgent().addFoodPlan(msg.getSender(), p));

          LOGGER.log(Level.INFO, "Get plan :" + newPlans);
        }

      } else {
        block();
      }
    } catch (UnreadableException e) {
      getAgent().sendStringReply(msg, ACLMessage.NOT_UNDERSTOOD, "( UnexpectedContent: " + e.getMessage() + ")");

      LOGGER.log(Level.WARNING, "Get understood message " + e.getMessage());
    }
  }

  private Optional<String> validateMessage(ACLMessage msg) {
    if (msg.getPerformative() != ACLMessage.INFORM) {
      return Optional.of("( (Unexpected-act " + ACLMessage.getPerformative(msg.getPerformative()) + ") )");
    }
    if (msg.getLanguage() == null || !msg.getLanguage().equals(SerShareConstants.JAVASERIALIZATION)) {
      return Optional.of("( (Unexpected-language " + msg.getLanguage() + ") )");
    }
    return Optional.empty();
  }

  public MerchantAgent getAgent() {
    return (MerchantAgent) myAgent;
  }
}
