package ru.romangr.simplestatemachine;

public interface EventsAwareBuilder<S extends Enum<S>, E extends Enum<E>> {

  InitialStateAwareBuilder<S, E> withEvents(Class<E> states);

}
