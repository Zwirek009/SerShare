package run;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import utils.SerShareConstants;


public class App {
    public static void main(String [] args) {
        jade.core.Runtime rt = jade.core.Runtime.instance();
        Properties props = new ExtendedProperties();
        props.setProperty(Profile.GUI, "true");
        props.setProperty("host", "192.168.111.1");
        props.setProperty("port", "8888");
        props.setProperty("platformId", "main");
        //Profile profile = new ProfileImpl("192.168.111.1", 8888, "main");
        Profile profile = new ProfileImpl(props);
        AgentContainer mainContainer = rt.createMainContainer(profile);

        //ContainerController container = rt.createAgentContainer(profile);
        try {
            String[] agentArgs = new String[] {};

            for (int noa = 0; noa < 3; ++noa) {
                AgentController ag = mainContainer.createNewAgent(SerShareConstants.MERCHANT_AGENT_NAME+noa,
                        SerShareConstants.MERCHANT_AGENT_CLASS_PATH,
                         agentArgs);//arguments
                ag.start();
            }
            for (int noa = 0; noa < 15; ++noa) {
                agentArgs = new String[] {SerShareConstants.MERCHANT_AGENT_NAME+(noa%3)};
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

            for (int noa = 0; noa < 30; ++noa) {
                agentArgs = new String[] {SerShareConstants.STOREKEEPER_AGENT_NAME+(noa%15), SerShareConstants.FRIDGE_AGENT_NAME+(noa%15)};
                AgentController ag = mainContainer.createNewAgent(SerShareConstants.MOBILE_AGENT_NAME+noa,
                        SerShareConstants.MOBILE_AGENT_CLASS_PATH,
                        agentArgs);//arguments
                ag.start();
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
