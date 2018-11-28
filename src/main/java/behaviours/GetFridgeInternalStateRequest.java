package behaviours;

import agents.FridgeAgent;
import jade.core.behaviours.Behaviour;

public class GetFridgeInternalStateRequest extends Behaviour {

    public GetFridgeInternalStateRequest(FridgeAgent fridgeAgent) {
        super(fridgeAgent);
    }

    @Override
    public void action() {
      //  myAgent.getAgentState().g KURWA jak z tego wziac stan lodowki od agenta jak typu nie mam ?

    }

    @Override
    public boolean done() {
        return false;
    }
}
