package agents.storekepper.behaviours;

import agents.storekepper.StorekeeperAgent;
import jade.core.behaviours.TickerBehaviour;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static agents.storekepper.StorekeeperAgent.LOGGER;

public class EstimateFridgeStatePlan extends TickerBehaviour {
  public EstimateFridgeStatePlan(StorekeeperAgent agent) {
    super(agent, TimeUnit.SECONDS.toMillis(15));
  }

  @Override
  protected void onTick() {
    LOGGER.log(Level.INFO, "Tick " + getAgent().getActionState());
    switch (getAgent().getActionState()) {
      case WAITING:
        getAgent().estimate();
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
