package behaviours;

import agents.FridgeAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;

public class GetFridgeInternalStateRequest extends Behaviour {

    public GetFridgeInternalStateRequest(FridgeAgent fridgeAgent, DataStore ds) {
        super(fridgeAgent);
        setDataStore(ds);
        Integer fridgeData = (Integer) getDataStore().get("FRIDGE_DATA");
        System.out.println("Check fridge :" + fridgeData);
    }

    @Override
    public void action() {
        //  System.out.println("Check fridge :" + myAgent.getAgentState().getValue());

    }

    @Override
    public boolean done() {
        return false;
    }
}
