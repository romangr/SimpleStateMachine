package ru.romangr.simplestatemachine;

public interface InitialStateAwareBuilder<S extends Enum<S>, E extends Enum<E>> {

  TransitionsBuilder<S, E> withInitialState(S state);
}
