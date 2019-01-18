package agents.mobile.behaviours;

import agents.SerShareAgent;
import agents.mobile.MobileAgent;
import customer.FoodPlan;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.SerShareConstants;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

import static agents.mobile.MobileAgent.LOGGER;

public class GetFoodPlan extends CyclicBehaviour {

    final int WAIT_FOR_REQUEST = 0;
    final int GET_FOOD_PLAN = 1;
    final int SEND_RESPONSE = 2;

    private int state = WAIT_FOR_REQUEST;
    private ACLMessage msgFromStorekeeper;
    private FoodPlan foodPlan;

    public GetFoodPlan(MobileAgent mobileAgent) {
        super(mobileAgent);
    }

    @Override
    public void action() {
        switch (state) {
            case WAIT_FOR_REQUEST:
                LOGGER.log(Level.INFO, "Waiting for food plan request.");
                ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId("food-plan"));

                if(msg != null) {
                    Optional<String> errors = validateMessage(msg);
                    if (msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {
                        onNotUnderstood();
                    } else if (errors.isPresent()) {
                        onErrors(msg, errors.get());
                    } else {
                        msgFromStorekeeper = msg;
                        LOGGER.log(Level.INFO, "Received food plan request " + msg);
                        state = GET_FOOD_PLAN;
                    }
                }
                else
                    block();
                break;

            case GET_FOOD_PLAN:
                foodPlan = ((MobileAgent)myAgent).getFoodPlan();
                LOGGER.log(Level.INFO, "Food plan ready to send.");
                state = SEND_RESPONSE;
                break;

            case SEND_RESPONSE:
                try {
                    ACLMessage response = createResponse();
                    myAgent.send(response);
                    LOGGER.log(Level.INFO, "Sent food plan response " + foodPlan);
                    state = WAIT_FOR_REQUEST;
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, e.getMessage());
                }
                break;
        }

    }

    private ACLMessage createResponse() throws IOException {
        ACLMessage reply = msgFromStorekeeper.createReply();
        reply.setPerformative(ACLMessage.INFORM_REF);
        reply.setContentObject(foodPlan);
        return reply;
    }

    protected Optional<String> validateMessage(ACLMessage msg) {
        if (msg.getPerformative() != ACLMessage.QUERY_REF) {
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
}
