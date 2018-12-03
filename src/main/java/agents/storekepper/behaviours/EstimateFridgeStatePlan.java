package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import jade.core.behaviours.CyclicBehaviour;

public class EstimateFridgeStatePlan extends CyclicBehaviour {
  public EstimateFridgeStatePlan(StorekeeperAgent agent) {
    super(agent);
  }

  public void action() {
    switch (getAgent().getActionState()) {
      case WAITING:
        break;
      case RUNNING:
        getAgent().estimate();
        break;
    }
  }

  public StorekeeperAgent getAgent() {
    return (StorekeeperAgent)myAgent;
  }
}
