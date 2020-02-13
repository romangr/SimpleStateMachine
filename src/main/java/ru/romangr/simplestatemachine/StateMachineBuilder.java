package ru.romangr.simplestatemachine;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import ru.romangr.simplestatemachine.nullability.NonNullApi;

@NonNullApi
public class StateMachineBuilder<S extends Enum<S>, E extends Enum<E>> implements
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
    return new StateMachine<>(states, events, initialState, new ArrayList<>(transitions));
  }

  static class Transition<S, E> {
    public final S fromState;
    public final S toState;
    public final E event;

    private Transition(S fromState, S toState, E event) {
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
  }
}
