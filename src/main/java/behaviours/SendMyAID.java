package behaviours;

import agents.SerShareAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import utils.SerShareConstants;

import java.util.List;
import java.util.logging.Level;

import static agents.mobile.MobileAgent.LOGGER;

public class SendMyAID extends OneShotBehaviour {

    private SerShareAgent anAgent;

    public SendMyAID (SerShareAgent anAgent) {
        super(anAgent);
        this.anAgent = anAgent;
    }

    @Override
    public void action() {
        List<AID> otherAgents = getAgentsToConnectWith();
        if (otherAgents.isEmpty())
            return;
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        for(AID agentID : otherAgents) {
            msg.addReceiver(agentID);
            LOGGER.log(Level.INFO, "Agent " + anAgent.getAID() + "sends AID to " + agentID + ".");
        }

        msg.setConversationId(getConversationId());
        msg.setReplyWith("aid");
        msg.setLanguage(SerShareConstants.JAVASERIALIZATION);
        anAgent.send(msg);
    }

    protected String getConversationId() {
        return "hello";
    }

    protected List<AID> getAgentsToConnectWith() {
        return anAgent.getOtherAgents();
    }

    public SerShareAgent getAnAgent() {
        return anAgent;
    }
}
