package agents.mobile;

import agents.SerShareAgent;
import agents.mobile.behaviours.GetFoodPlan;
import customer.FoodPlan;
import customer.FoodPlanManager;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import roles.CustomerRole;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import static utils.SerShareConstants.MOBILE_AGENT_NAME;

public class MobileAgent extends SerShareAgent implements CustomerRole {

    public static final Logger LOGGER = Logger.getLogger( MobileAgent.class.getName() );

    private FoodPlanManager foodPlanManager;

    protected void setup() {
        super.setup();
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        this.foodPlanManager = new FoodPlanManager();
        addBehaviour(new GetFoodPlan(this));
       // addBehaviour(new ShareResponse(this));

        ServiceDescription sd  = new ServiceDescription();
        sd.setType( MOBILE_AGENT_NAME );
        sd.setName( getLocalName() );
        register( sd );
    }

    public FoodPlan getFoodPlan() {
        return foodPlanManager.getPlan();
    }
}
