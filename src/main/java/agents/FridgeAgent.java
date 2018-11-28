package agents;

import behaviours.GetFridgeInternalStateRequest;
import db.FridgeAPI;
import jade.core.behaviours.CyclicBehaviour;
import roles.FridgeStateController;

import java.util.Map;

public class FridgeAgent extends SerShareAgent implements FridgeStateController {

    public FridgeAPI fridgeAPI;
    public Map<String, Integer> fridgeState;

    protected void setup() {
        super.setup();
        // Add the CyclicBehaviour

        fridgeAPI = new FridgeAPI();

        addBehaviour(new CheckFridgeBehaviour(this) {
            public void action() {
                System.out.println("Cycling");
            }
        });

        addBehaviour(new GetFridgeInternalStateRequest(this));

    }

    class CheckFridgeBehaviour extends CyclicBehaviour {
        CheckFridgeBehaviour(FridgeAgent fridgeAgent) {
            super(fridgeAgent);
        }

        public void action() {

            System.out.println("Check fridge");
            fridgeAPI.getData();
        }
    }

}
