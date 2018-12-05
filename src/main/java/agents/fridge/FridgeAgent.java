package agents.fridge;

import agents.SerShareAgent;
import agents.fridge.behaviours.CheckFridgeInternals;
import agents.fridge.behaviours.GetFridgeInternalStateResponse;
import db.FridgeStore;
import jade.core.behaviours.DataStore;
import roles.FridgeStateController;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class FridgeAgent extends SerShareAgent implements FridgeStateController {

    public static final Logger LOGGER = Logger.getLogger( FridgeAgent.class.getName() );
    public static final String FRIDGE_DATA = "FRIDGE_DATA";


    private FridgeStore fridgeStore;

    protected void setup() {
        super.setup();
        // Add the CyclicBehaviour

        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));

        this.fridgeStore = new FridgeStore();

        DataStore commonDataStore = new DataStore();
        addBehaviour(new CheckFridgeInternals(this, commonDataStore));
        addBehaviour(new GetFridgeInternalStateResponse(this, commonDataStore));

    }

    public void setFridgeStore(FridgeStore fridgeStore) {
        this.fridgeStore = fridgeStore;
    }

    public FridgeStore getFridgeStore() {
        return fridgeStore;
    }
}
