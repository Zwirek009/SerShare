package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import customer.FoodPlan;
import db.FridgeStore;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.SerShareConstants;

import java.io.Serializable;
import java.util.Optional;
import java.util.logging.Level;

import static agents.storekepper.StorekeeperAgent.LOGGER;

public class SendFridgeStateRequest extends Behaviour {
      private MessageTemplate mt; // The template to receive replies
      private int step = 0;

      public SendFridgeStateRequest(StorekeeperAgent agent) {
        super(agent);
  }

  public void action() {
    switch (step) {
      case 0:
        // wysłanie zapytania do wszystkich
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        for (AID m : getAgent().getFridges()) {
          cfp.addReceiver(m);
        }
        cfp.setConversationId("fridge-state");
        cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
        cfp.setLanguage(SerShareConstants.JAVASERIALIZATION);
        myAgent.send(cfp);

        // Prepare the template to get proposals
        mt = MessageTemplate.and(MessageTemplate.MatchConversationId("fridge-state"),
            MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
        step = 1;

        LOGGER.log(Level.INFO, "Send fridge state request");
        break;
      case 1:
        // pobranie wszystkich odpowiedzi
        ACLMessage reply = myAgent.receive(mt);
        try {
          if (reply != null) {
            Optional<String> errors = validateMessage(reply);
            if (errors.isPresent()) {
              getAgent().sendStringReply(reply, ACLMessage.NOT_UNDERSTOOD, errors.get());

              LOGGER.log(Level.WARNING, errors.get());
            } else {
              //get fridge state
              FridgeStore store = (FridgeStore) reply.getContentObject();
              getAgent().setFridgeStore(store);
              LOGGER.log(Level.INFO, "Get fridge state " + store);
            }
            if (getAgent().hasAllFridgeStates()) {
              LOGGER.log(Level.INFO, "Get fridge state");
              step = 2;
            }
          } else {
            block();
          }
        } catch (UnreadableException e) {
          getAgent().sendStringReply(reply, ACLMessage.NOT_UNDERSTOOD, "( UnexpectedContent: " + e.getMessage() + ")");
          LOGGER.log(Level.WARNING, "Error when try send messages " + e.getMessage());
        }
        break;
    }
  }

  private Optional<String> validateMessage(ACLMessage msg) {
    if (msg.getPerformative() != ACLMessage.INFORM) {
      return Optional.of("( (Unexpected-act " + ACLMessage.getPerformative(msg.getPerformative()) + ") )");
    }
    if (!msg.getLanguage().equals(SerShareConstants.JAVASERIALIZATION)) {
      return Optional.of("( (Unexpected-language " + msg.getLanguage() + ") )");
    }
    return Optional.empty();
  }

  public StorekeeperAgent getAgent() {
    return (StorekeeperAgent)myAgent;
  }

  public boolean done() {
    return step == 2;
  }
}
