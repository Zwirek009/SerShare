package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.SerShareConstants;

import java.util.Optional;
import java.util.logging.Level;

import static agents.storekepper.StorekeeperAgent.LOGGER;

public class GetMobileAid extends CyclicBehaviour {
  public GetMobileAid(StorekeeperAgent agent) {
    super(agent);
  }

  public void action() {
    ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId("hello"));

    if (msg != null) {
      Optional<String> errors = validateMessage(msg);
      if(msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {
        LOGGER.log(Level.INFO, "Not understood");
      } else if (errors.isPresent()) {
        getAgent().sendStringReply(msg, ACLMessage.NOT_UNDERSTOOD, errors.get());

        LOGGER.log(Level.WARNING, errors.get());
      } else {
        getAgent().addNewMobile(msg.getSender());

        LOGGER.log(Level.INFO, "Get new mobile " + msg.getSender());
      }

    } else {
      block();
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

  public StorekeeperAgent getAgent() {
    return (StorekeeperAgent) myAgent;
  }
}
