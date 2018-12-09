package agents.fridge;

import agents.SerShareAgent;
import agents.fridge.behaviours.*;
import behaviours.SendMyAID;
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

        //tak musialem dodac aid, zeby wiadomosc doszla
/*        AID aid = new AID("mobile@192.168.0.192:1099/JADE", true);
        aid.addAddresses("http://modzel101:7778/acc");
        System.out.println(aid);
        mobiles.add(aid);*/
        this.fridgeStore = new FridgeStore();

        DataStore commonDataStore = new DataStore();
        addBehaviour(new SendFridgeAID(this));
        addBehaviour(new GetMobileAIDForFridge(this));
        addBehaviour(new CheckFridgeInternals(this, commonDataStore));
        addBehaviour(new GetFridgeInternalStateResponse(this, commonDataStore));
        sendShareRequest(new FoodProduct("Mleko", 2.0));
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

    public void addNewMobile(AID newMobile) { if(!this.mobiles.contains(newMobile)) this.mobiles.add(newMobile);}

}
