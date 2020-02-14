package ru.romangr.simplestatemachine;

import java.util.Collections;
import java.util.Map;
import ru.romangr.simplestatemachine.nullability.NonNullApi;

@NonNullApi
public class StateMachine<S extends Enum<S>, E extends Enum<E>> {

  private final S initialState;
  private Map<S, Map<E, Transition<S, E>>> transitionsByState;
  private S currentState;

  public StateMachine(S initialState, Map<S, Map<E, Transition<S, E>>> transitionsByState) {
    this(initialState, initialState, transitionsByState);
  }

  public StateMachine(S initialState, S currentState, Map<S, Map<E, Transition<S, E>>> transitionsByState) {
    this.currentState = currentState;
    this.initialState = initialState;
    this.transitionsByState = Collections.unmodifiableMap(transitionsByState);
  }

  public final EventAcceptResult<S> acceptEvent(E event) {
    Map<E, Transition<S, E>> transitionsByEvent = transitionsByState.get(currentState);
    if (transitionsByEvent == null) {
      return acceptResultForUnexpectedEvent();
    }
    Transition<S, E> transition = transitionsByEvent.get(event);
    if (transition == null) {
      return acceptResultForUnexpectedEvent();
    }
    currentState = transition.toState;
    return new EventAcceptResult<>(currentState, EventAcceptStatus.SUCCESS);
  }

  public final S currentState() {
    return currentState;
  }

  public final void reset() {
    this.currentState = initialState;
  }

  public static <S extends Enum<S>, E extends Enum<E>> StatesAwareBuilder<S, E> builder() {
    return new StateMachineBuilder<>();
  }

  private EventAcceptResult<S> acceptResultForUnexpectedEvent() {
    return new EventAcceptResult<>(currentState, EventAcceptStatus.UNEXPECTED_EVENT);
  }

}
