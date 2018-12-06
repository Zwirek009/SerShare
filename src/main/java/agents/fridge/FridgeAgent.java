package agents.fridge;

import agents.SerShareAgent;
import agents.fridge.behaviours.CheckFridgeInternals;
import agents.fridge.behaviours.GetFridgeInternalStateResponse;
import agents.fridge.behaviours.ShareRequest;
import db.FoodProduct;
import db.FridgeStore;
import jade.core.AID;
import jade.core.behaviours.DataStore;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import roles.FridgeStateController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class FridgeAgent extends SerShareAgent implements FridgeStateController {

    public static final Logger LOGGER = Logger.getLogger( FridgeAgent.class.getName() );
    public static final String FRIDGE_DATA = "FRIDGE_DATA";

    private List<AID> mobiles;
    private FridgeStore fridgeStore;

    protected void setup() {
        super.setup();
        // Add the CyclicBehaviour
        LOGGER.info(getAID().toString());
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));

        mobiles = new ArrayList<>();
        this.fridgeStore = new FridgeStore();

        DataStore commonDataStore = new DataStore();
        addBehaviour(new CheckFridgeInternals(this, commonDataStore));
        addBehaviour(new GetFridgeInternalStateResponse(this, commonDataStore));
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
        return this.mobiles;
    }
}
