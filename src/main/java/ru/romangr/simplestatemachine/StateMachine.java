package ru.romangr.simplestatemachine;

import java.util.Map;
import ru.romangr.simplestatemachine.nullability.NonNullApi;

/**
 * This is a simple implementation of state machine.
 *
 * All information about transitions is stored in-memory.
 *
 * The implementation is not thread safe.
 */
@NonNullApi
public class StateMachine<S extends Enum<S>, E extends Enum<E>> {

  private final StateMachineConfiguration<S, E> configuration;
  private S currentState;

  private StateMachine(StateMachineConfiguration<S, E> configuration, S currentState) {
    this.currentState = currentState;
    this.configuration = configuration;
  }

  public static <S extends Enum<S>, E extends Enum<E>> StateMachine<S, E> fromInitialState(
      StateMachineConfiguration<S, E> configuration
  ) {
    return new StateMachine<>(configuration, configuration.getInitialState());
  }

  public static <S extends Enum<S>, E extends Enum<E>> StateMachine<S, E> fromState(
      StateMachineConfiguration<S, E> configuration, S currentState
  ) {
    return new StateMachine<>(configuration, currentState);
  }

  /**
   * Passes the event to the state machine. Doesn't throw exceptions.
   *
   * @param event to accept.
   * @return {@link EventAcceptResult}
   */
  public final EventAcceptResult<S> acceptEvent(E event) {
    Map<E, Transition<S, E>> transitionsByEvent = configuration.transitionsByState.get(currentState);
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
    this.currentState = configuration.initialState;
  }

  public static <S extends Enum<S>, E extends Enum<E>> StatesAwareBuilder<S, E> builder() {
    return new StateMachineBuilder<>();
  }

  private EventAcceptResult<S> acceptResultForUnexpectedEvent() {
    return new EventAcceptResult<>(currentState, EventAcceptStatus.UNEXPECTED_EVENT);
  }

}
