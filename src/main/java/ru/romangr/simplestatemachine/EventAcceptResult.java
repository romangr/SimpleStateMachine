package ru.romangr.simplestatemachine;

public class EventAcceptResult<S extends Enum<S>> {
  private final S newState;
  private final EventAcceptStatus result;

  public EventAcceptResult(S newState, EventAcceptStatus result) {
    this.newState = newState;
    this.result = result;
  }

  public S newState() {
    return newState;
  }

  public EventAcceptStatus result() {
    return result;
  }
}
