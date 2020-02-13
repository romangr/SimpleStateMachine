package ru.romangr.simplestatemachine;

import java.util.List;
import ru.romangr.simplestatemachine.StateMachineBuilder.Transition;
import ru.romangr.simplestatemachine.nullability.NonNullApi;

@NonNullApi
public class StateMachine<S extends Enum<S>, E extends Enum<E>> {

  StateMachine(Class<S> states, Class<E> events, S state,
      List<Transition<S, E>> transitions) {

  }

  public S currentState() {
    return null;
  }

  public static <S extends Enum<S>, E extends Enum<E>> StatesAwareBuilder<S, E> builder() {
    return new StateMachineBuilder<>();
  }

  public EventAcceptResult<S> acceptEvent(E event) {
    return new EventAcceptResult<>(currentState(), EventAcceptStatus.SUCCESS);
  }
}
