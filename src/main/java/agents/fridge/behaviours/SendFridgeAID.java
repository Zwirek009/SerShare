package agents.fridge.behaviours;

import agents.fridge.FridgeAgent;
import behaviours.SendMyAID;

public class SendFridgeAID extends SendMyAID {

    public SendFridgeAID (FridgeAgent anAgent) {
        super(anAgent);
    }

    @Override
    protected String getConversationId() {
        return "hello-from-fridge";
    }
}
