package ru.romangr.simplestatemachine;

public interface TransitionsBuilder<S extends Enum<S>, E extends Enum<E>> {

  TransitionsBuilder<S, E> withTransition(S one, S two, E event);

  StateMachine<S, E> build();
}
