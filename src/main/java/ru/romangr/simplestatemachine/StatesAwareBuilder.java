package ru.romangr.simplestatemachine;

public interface StatesAwareBuilder<S extends Enum<S>, E extends Enum<E>> {

  EventsAwareBuilder<S, E> withStates(Class<S> states);

}
