package ru.romangr.simplestatemachine;

import java.util.Collections;
import java.util.Map;
import ru.romangr.simplestatemachine.nullability.NonNullApi;

@NonNullApi
public final class StateMachineConfiguration<S extends Enum<S>, E extends Enum<E>> {
  final Map<S, Map<E, Transition<S, E>>> transitionsByState;
  final S initialState;

  public StateMachineConfiguration(
      Map<S, Map<E, Transition<S, E>>> transitionsByState, S initialState) {
    this.transitionsByState = Collections.unmodifiableMap(transitionsByState);
    this.initialState = initialState;
  }

  public Map<S, Map<E, Transition<S, E>>> getTransitionsByState() {
    return transitionsByState;
  }

  public S getInitialState() {
    return initialState;
  }
}
