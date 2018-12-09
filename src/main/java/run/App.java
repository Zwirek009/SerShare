package run;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import utils.SerShareConstants;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;


public class App {

    public static final Logger LOGGER = Logger.getLogger( App.class.getName() );

    public static void main(String [] args) {
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));

        jade.core.Runtime rt = jade.core.Runtime.instance();

        Properties props = new ExtendedProperties();
        props.setProperty(Profile.GUI, "true");
        props.setProperty("host", "localhost");
        props.setProperty("port", "8888");
        props.setProperty("platformId", "main");
        Profile profile = new ProfileImpl(props);

        AgentContainer mainContainer = rt.createMainContainer(profile);

        try {
            String[] agentArgs = new String[] {};

            for (int noa = 0; noa < SerShareConstants.NUMBER_OF_MERCHANT_AGENTS; ++noa) {
                AgentController ag = mainContainer.createNewAgent(SerShareConstants.MERCHANT_AGENT_NAME+noa,
                        SerShareConstants.MERCHANT_AGENT_CLASS_PATH,
                         agentArgs);//arguments
                ag.start();
            }
            for (int noa = 0; noa < SerShareConstants.NUMBER_OF_FRIDGE_AGENTS; ++noa) {
                agentArgs = new String[] {SerShareConstants.MERCHANT_AGENT_NAME+(noa%SerShareConstants.NUMBER_OF_MERCHANT_AGENTS)};
                AgentController sag = mainContainer.createNewAgent(SerShareConstants.STOREKEEPER_AGENT_NAME+noa,
                        SerShareConstants.STOREKEEPER_AGENT_CLASS_PATH,
                        agentArgs);//arguments
                sag.start();
                agentArgs = new String[] {SerShareConstants.STOREKEEPER_AGENT_NAME+noa};
                AgentController fag = mainContainer.createNewAgent(SerShareConstants.FRIDGE_AGENT_NAME+noa,
                        SerShareConstants.FRIDGE_AGENT_CLASS_PATH,
                        agentArgs);//arguments
                fag.start();
            }

            for (int noa = 0; noa < SerShareConstants.NUMBER_OF_MOBILE_AGENTS; ++noa) {
                agentArgs = new String[] {SerShareConstants.STOREKEEPER_AGENT_NAME+(noa%SerShareConstants.NUMBER_OF_FRIDGE_AGENTS), SerShareConstants.FRIDGE_AGENT_NAME+(noa%SerShareConstants.NUMBER_OF_FRIDGE_AGENTS)};
                AgentController ag = mainContainer.createNewAgent(SerShareConstants.MOBILE_AGENT_NAME+noa,
                        SerShareConstants.MOBILE_AGENT_CLASS_PATH,
                        agentArgs);//arguments
                ag.start();
            }
        } catch (StaleProxyException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }
}
