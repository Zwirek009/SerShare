package agents.fridge.behaviours;

import agents.fridge.FridgeAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;

public class GetFridgeInternalStateResponse extends Behaviour {

    public GetFridgeInternalStateResponse(FridgeAgent fridgeAgent, DataStore ds) {
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
