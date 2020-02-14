package ru.romangr.simplestatemachine;

import java.util.Map;

public interface TransitionsBuilder<S extends Enum<S>, E extends Enum<E>> {

  TransitionsBuilder<S, E> withTransition(S one, S two, E event);

  StateMachine<S, E> build();

  Map<S, Map<E, Transition<S, E>>> asTransitionsMap();
}
