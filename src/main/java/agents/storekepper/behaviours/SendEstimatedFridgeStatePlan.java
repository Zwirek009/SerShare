package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class SendEstimatedFridgeStatePlan extends Behaviour {
  public SendEstimatedFridgeStatePlan(StorekeeperAgent agent) {
    super(agent);
  }

  public void action() {
    ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
    inform.addReceiver(getAgent().getMerchantAgent());
    try {
      inform.setContentObject(getAgent().getPlans().toArray());
    } catch (IOException e) {
      e.printStackTrace();
    }
    getAgent().send(inform);
  }

  public StorekeeperAgent getAgent() {
    return (StorekeeperAgent)myAgent;
  }

  public boolean done() {
    return true;
  }
}
