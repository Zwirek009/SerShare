package behaviours;

import agents.FridgeAgent;
import agents.MobileAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;

public class GetFoodPlanRequest extends Behaviour {

    public GetFoodPlanRequest(MobileAgent mobileAgent, DataStore ds) {
        super(mobileAgent);
        setDataStore(ds);
        String foodPlanData = (String) getDataStore().get("FOOD_PLAN_DATA");
        System.out.println("Check foodPlan :" + foodPlanData);
    }


    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }
}
