package agents.fridge;

import agents.SerShareAgent;
import agents.fridge.behaviours.*;
import db.FoodProduct;
import db.FridgeStore;
import jade.core.AID;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import roles.FridgeStateController;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import static utils.SerShareConstants.FRIDGE_AGENT_NAME;
import static utils.SerShareConstants.MOBILE_AGENT_NAME;

public class FridgeAgent extends SerShareAgent implements FridgeStateController {

    public static final Logger LOGGER = Logger.getLogger( FridgeAgent.class.getName() );
    public static final String FRIDGE_DATA = "FRIDGE_DATA";

    private FridgeStore fridgeStore;

    protected void setup() {
        super.setup();
        // Add the CyclicBehaviour
        LOGGER.info(getAID().toString());
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));

        this.fridgeStore = new FridgeStore();

        DataStore commonDataStore = new DataStore();

        addBehaviour(new CheckFridgeInternals(this, commonDataStore));
        addBehaviour(new GetFridgeInternalStateResponse(this, commonDataStore));

        ServiceDescription sd  = new ServiceDescription();
        sd.setType( FRIDGE_AGENT_NAME );
        sd.setName( getLocalName() );
        register( sd );
    }

    public void sendShareRequest (FoodProduct prod) {
        addBehaviour(new ShareRequest(this, prod));
    }

    public void setFridgeStore(FridgeStore fridgeStore) {
        this.fridgeStore = fridgeStore;
    }

    public FridgeStore getFridgeStore() {
        return fridgeStore;
    }

    public List<AID> getMobiles() {
        return Arrays.asList(this.searchDF(MOBILE_AGENT_NAME));
    }

}
