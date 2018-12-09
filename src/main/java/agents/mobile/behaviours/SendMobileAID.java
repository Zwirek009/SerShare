package agents.mobile.behaviours;

import agents.SerShareAgent;
import agents.mobile.MobileAgent;
import behaviours.SendMyAID;
import jade.core.AID;

import java.util.List;

public class SendMobileAID extends SendMyAID {

    public SendMobileAID (MobileAgent anAgent) {
        super(anAgent);
    }

    @Override
    protected String getConversationId() {
        return "hello-from-mobile";
    }
}
