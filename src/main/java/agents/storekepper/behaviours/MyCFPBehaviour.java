package agents.storekepper.behaviours;

import agents.SerShareAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.SerShareConstants;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public abstract class MyCFPBehaviour extends CyclicBehaviour {
  public static final Logger LOGGER = Logger.getLogger(MyCFPBehaviour.class.getName());

  private MessageTemplate mt; // The template to receive replies
  private int step = 0;
  private int receiversWithoutReplyNr = 0;
  private String conversaionId;

  public MyCFPBehaviour(SerShareAgent agent, String conversationId) {
    super(agent);
    LOGGER.setLevel(Level.ALL);
    LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
    this.conversaionId = conversationId;
  }

  public void action() {
    switch (step) {
      case 0:
        sendRequest();
        step = 1;

        LOGGER.log(Level.INFO, "Send " + conversaionId + " request");
        break;
      case 1:
        ACLMessage reply = myAgent.receive(mt);

        if (reply != null) {
          getReply(reply);
        } else {
          block();
        }

        if (receiversWithoutReplyNr == 0) {
          mt = null;
          step = 0;
        }
        break;
    }
  }

  private void sendRequest() {
    // wysłanie zapytania do wszystkich
    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);

    for (AID m : getReceivers()) {
      receiversWithoutReplyNr++;
      cfp.addReceiver(m);
    }
    cfp.setConversationId(conversaionId);
    cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
    cfp.setLanguage(SerShareConstants.JAVASERIALIZATION);
    myAgent.send(cfp);

    // Prepare the template to get proposals
    mt = MessageTemplate.and(MessageTemplate.MatchConversationId(conversaionId),
        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));


    LOGGER.log(Level.INFO, "Send cfp with id: " + conversaionId);
  }

  private void getReply(ACLMessage reply) {
    Optional<String> errors = validateMessage(reply);
    if (reply.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {
      onNotUnderstood();
    } else if (errors.isPresent()) {
      onErrors(reply, errors.get());
    } else {
      onGettingReply(reply);
      receiversWithoutReplyNr--;
    }

    LOGGER.log(Level.INFO, "Reply with id: " + conversaionId);
  }

  protected Optional<String> validateMessage(ACLMessage msg) {
    if (msg.getPerformative() != ACLMessage.INFORM) {
      return Optional.of("( (Unexpected-act " + ACLMessage.getPerformative(msg.getPerformative()) + ") )");
    }
    if (msg.getLanguage() == null || !msg.getLanguage().equals(SerShareConstants.JAVASERIALIZATION)) {
      return Optional.of("( (Unexpected-language " + msg.getLanguage() + ") )");
    }
    return Optional.empty();
  }

  protected void onNotUnderstood() {
    LOGGER.log(Level.INFO, "Not understood");
  }

  protected void onErrors(ACLMessage reply, String error) {
    ((SerShareAgent) getAgent()).sendStringReply(reply, ACLMessage.NOT_UNDERSTOOD, error);

    LOGGER.log(Level.WARNING, error);
  }

  @Override
  public void reset() {
    super.reset();
    sendRequest();
  }

  abstract protected List<AID> getReceivers();

  abstract protected void onGettingReply(ACLMessage reply);
}