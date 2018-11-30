package agents;

import behaviours.GetFridgeInternalStateRequest;
import db.FridgeAPI;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import roles.FridgeStateController;

public class FridgeAgent extends SerShareAgent implements FridgeStateController {

    public FridgeAPI fridgeAPI;

    protected void setup() {
        super.setup();
        // Add the CyclicBehaviour
        fridgeAPI = new FridgeAPI();
        DataStore commonDataStore = new DataStore();
        addBehaviour(new CheckFridgeBehaviour(this, commonDataStore));
        addBehaviour(new GetFridgeInternalStateRequest(this, commonDataStore));

    }

    class CheckFridgeBehaviour extends CyclicBehaviour {
        CheckFridgeBehaviour(FridgeAgent fridgeAgent, DataStore ds) {
            super(fridgeAgent);
            setDataStore(ds);
            getDataStore().put("FRIDGE_DATA", fridgeAPI.getData());
        }

        public void action() {
          //  getDataStore().put("FRIDGE_DATA", fridgeAPI.getData());
        }
    }

}
