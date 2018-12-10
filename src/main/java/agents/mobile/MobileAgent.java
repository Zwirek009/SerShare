package agents.mobile;

import agents.SerShareAgent;
import agents.mobile.behaviours.GetFoodPlan;
import agents.mobile.behaviours.SendMobileAID;
import agents.mobile.behaviours.ShareResponse;
import behaviours.SendMyAID;
import customer.FoodPlan;
import customer.FoodPlanManager;
import jade.core.AID;
import roles.CustomerRole;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class MobileAgent extends SerShareAgent implements CustomerRole {

    public static final Logger LOGGER = Logger.getLogger( MobileAgent.class.getName() );

    private FoodPlanManager foodPlanManager;

    protected void setup() {
        super.setup();
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        this.foodPlanManager = new FoodPlanManager();
        addBehaviour(new SendMobileAID(this));
        addBehaviour(new GetFoodPlan(this));
       // addBehaviour(new ShareResponse(this));
    }

    public FoodPlan getFoodPlan() {
        return foodPlanManager.getPlan();
    }
}
