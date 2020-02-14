package ru.romangr.simplestatemachine;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import ru.romangr.simplestatemachine.nullability.NonNullApi;

@NonNullApi
public final class StateMachineBuilder<S extends Enum<S>, E extends Enum<E>> implements
    StatesAwareBuilder<S, E>, EventsAwareBuilder<S, E>, TransitionsBuilder<S, E>,
    InitialStateAwareBuilder<S, E> {

  @Nullable
  private Class<S> states;

  @Nullable
  private Class<E> events;

  private final Set<Transition<S, E>> transitions = new HashSet<>();

  @Nullable
  private S initialState;

  @Override
  public InitialStateAwareBuilder<S, E> withEvents(Class<E> events) {
    this.events = events;
    return this;
  }

  @Override
  public EventsAwareBuilder<S, E> withStates(Class<S> states) {
    this.states = states;
    return this;
  }

  @Override
  public TransitionsBuilder<S, E> withInitialState(S state) {
    this.initialState = state;
    return this;
  }

  @Override
  public TransitionsBuilder<S, E> withTransition(S from, S to, E event) {
    boolean isAdded = transitions.add(new Transition<>(from, to, event));
    if (!isAdded) {
      throw new IllegalArgumentException("Duplicated transitions are not allowed!");
    }
    return this;
  }

  @Override
  public StateMachine<S, E> build() {
    requireNonNull(states);
    requireNonNull(events);
    requireNonNull(initialState);
    return new StateMachine<>(initialState, collectTransitionsMap());
  }

  @Override
  public Map<S, Map<E, Transition<S, E>>> asTransitionsMap() {
    return Collections.unmodifiableMap(collectTransitionsMap());
  }

  private Map<S, Map<E, Transition<S, E>>> collectTransitionsMap() {
    return transitions.stream()
        .collect(toMap(t -> t.fromState, this::collectTransitionsMap, this::mergeTransitionMaps));
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
