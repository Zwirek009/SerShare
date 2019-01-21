package agents.fridge.behaviours;

import agents.fridge.FridgeAgent;
import db.FridgeAPI;
import db.FridgeStore;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.TickerBehaviour;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static agents.fridge.FridgeAgent.FRIDGE_DATA;
import static agents.fridge.FridgeAgent.LOGGER;

public class CheckFridgeInternals extends TickerBehaviour {

    public FridgeAPI fridgeAPI;

    public CheckFridgeInternals(FridgeAgent fridgeAgent, DataStore ds) {
        super(fridgeAgent, TimeUnit.SECONDS.toMillis(32));
        fridgeAPI = new FridgeAPI();
        setDataStore(ds);
    }

    @Override
    protected void onTick() {
        FridgeStore fs = fridgeAPI.getStore();
        getDataStore().put(FRIDGE_DATA, fs);
        getAgent().setFridgeStore(fs);
        LOGGER.log(Level.INFO, "Updated fridge state: " + fs);
    }

    public FridgeAgent getAgent() {
        return (FridgeAgent) myAgent;
    }
}
