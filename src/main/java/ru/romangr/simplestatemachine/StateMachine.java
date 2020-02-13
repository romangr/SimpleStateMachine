package ru.romangr.simplestatemachine;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.romangr.simplestatemachine.StateMachineBuilder.Transition;
import ru.romangr.simplestatemachine.nullability.NonNullApi;

@NonNullApi
public class StateMachine<S extends Enum<S>, E extends Enum<E>> {

  private final S[] states;
  private final E[] events;
  private Map<S, Map<E, Transition<S, E>>> transitionsByState;
  private S currentState;

  StateMachine(Class<S> states, Class<E> events, S initialState,
      List<Transition<S, E>> transitions) {
    this.currentState = initialState;
    this.states = states.getEnumConstants();
    this.events = events.getEnumConstants();
    transitionsByState = transitions.stream()
        .collect(toMap(t -> t.fromState, this::collectTransitionsMap, this::mergeTransitionMaps));
  }

  public S currentState() {
    return currentState;
  }

  public EventAcceptResult<S> acceptEvent(E event) {
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

  public static <S extends Enum<S>, E extends Enum<E>> StatesAwareBuilder<S, E> builder() {
    return new StateMachineBuilder<>();
  }

  private EventAcceptResult<S> acceptResultForUnexpectedEvent() {
    return new EventAcceptResult<>(currentState, EventAcceptStatus.UNEXPECTED_EVENT);
  }

  private Map<E, Transition<S, E>> collectTransitionsMap(Transition<S, E> transition) {
    Map<E, Transition<S, E>> map = new HashMap<>();
    map.put(transition.event, transition);
    return map;
  }

  private Map<E, Transition<S, E>> mergeTransitionMaps(Map<E, Transition<S, E>> map1,
      Map<E, Transition<S, E>> map2) {
    map2.forEach((k, v) -> {
      Transition<S, E> previousValue = map1.put(k, v);
      if (previousValue != null) {
        throw new IllegalStateException(
            "Ambiguous transitions: '" + v + "' and '" + previousValue + "'");
      }
    });
    return map1;
  }
}
