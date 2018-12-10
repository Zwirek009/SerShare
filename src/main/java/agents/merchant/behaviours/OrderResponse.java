package agents.merchant.behaviours;

import jade.core.AID;

import java.time.LocalDate;
import java.util.List;

public class OrderResponse {
  private LocalDate date;
  private List<AID> receivers;

  public OrderResponse(LocalDate date, List<AID> receivers) {
    this.date = date;
    this.receivers = receivers;
  }

  public LocalDate getDate() {
    return date;
  }

  public List<AID> getReceivers() {
    return receivers;
  }
}
