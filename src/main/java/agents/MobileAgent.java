package agents;

import behaviours.GetFoodPlanRequest;
import behaviours.GetFridgeInternalStateRequest;
import customer.FoodPlanManager;
import db.FridgeAPI;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import roles.CustomerRole;

public class MobileAgent extends SerShareAgent implements CustomerRole {
    public FoodPlanManager foodPlanManager;

    protected void setup() {
        super.setup();
        // Add the CyclicBehaviour
        foodPlanManager = new FoodPlanManager();
        DataStore commonDataStore = new DataStore();
        addBehaviour(new MobileAgent.CheckFoodPlanBehaviour(this, commonDataStore));
        addBehaviour(new GetFoodPlanRequest(this, commonDataStore));

    }

    class CheckFoodPlanBehaviour extends Behaviour {
        public CheckFoodPlanBehaviour(MobileAgent mobileAgent, DataStore ds) {
            super(mobileAgent);
            setDataStore(ds);
            getDataStore().put("FOOD_PLAN_DATA", foodPlanManager.getPlan());
        }

        @Override
        public void action() {

        }

        @Override
        public boolean done() {
            return false;
        }
    }
}
