package agents.fridge.behaviours;

import agents.fridge.FridgeAgent;
import db.FridgeAPI;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.TickerBehaviour;

import java.util.concurrent.TimeUnit;

import static agents.fridge.FridgeAgent.FRIDGE_DATA;

public class CheckFridgeInternals extends TickerBehaviour {

    public FridgeAPI fridgeAPI;

    public CheckFridgeInternals(FridgeAgent fridgeAgent, DataStore ds) {
        super(fridgeAgent, TimeUnit.MINUTES.toMillis(1));
        fridgeAPI = new FridgeAPI();
        setDataStore(ds);
    }

    @Override
    protected void onTick() {
        getDataStore().put(FRIDGE_DATA, fridgeAPI.getStore());
    }
}
