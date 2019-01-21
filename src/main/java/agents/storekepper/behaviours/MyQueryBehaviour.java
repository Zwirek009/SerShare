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

public abstract class MyQueryBehaviour extends CyclicBehaviour {
  public static final Logger LOGGER = Logger.getLogger(MyQueryBehaviour.class.getName());

  private MessageTemplate mt; // The template to receive replies
  private int step = 0;
  private int receiversWithoutReplyNr = 0;
  private String conversaionId;

  public MyQueryBehaviour(SerShareAgent agent, String conversationId) {
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
        }
        break;
    }
  }

  private void sendRequest() {
    // wysłanie zapytania do wszystkich
    ACLMessage query_ref = new ACLMessage(ACLMessage.QUERY_REF);
    List<AID> receivers = getReceivers();
      if (receivers.isEmpty())
        LOGGER.log(Level.WARNING, "No receivers for message!");
    for (AID m : getReceivers()) {
      receiversWithoutReplyNr++;
      query_ref.addReceiver(m);
    }
    query_ref.setConversationId(conversaionId);
    query_ref.setReplyWith("query_ref" + System.currentTimeMillis()); // Unique value
    query_ref.setLanguage(SerShareConstants.JAVASERIALIZATION);
    myAgent.send(query_ref);

    // Prepare the template to get proposals
    mt = MessageTemplate.and(MessageTemplate.MatchConversationId(conversaionId),
        MessageTemplate.MatchInReplyTo(query_ref.getReplyWith()));


    LOGGER.log(Level.INFO, "Send query_ref with id: " + conversaionId);
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

    LOGGER.log(Level.INFO, "Got reply: " + reply);
  }

  protected Optional<String> validateMessage(ACLMessage msg) {
    if (msg.getPerformative() != ACLMessage.INFORM_REF) {
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