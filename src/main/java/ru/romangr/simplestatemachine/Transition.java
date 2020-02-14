package ru.romangr.simplestatemachine;

import java.util.Objects;

public final class Transition<S extends Enum<S>, E extends Enum<E>> {

  public final S fromState;
  public final S toState;
  public final E event;

  Transition(S fromState, S toState, E event) {
    this.fromState = fromState;
    this.toState = toState;
    this.event = event;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transition<?, ?> that = (Transition<?, ?>) o;
    return fromState.equals(that.fromState) &&
        toState.equals(that.toState) &&
        event.equals(that.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromState, toState, event);
  }

  @Override
  public String toString() {
    return "{ " + fromState + " ---" + event + "--> " + toState + " }";
  }
}
