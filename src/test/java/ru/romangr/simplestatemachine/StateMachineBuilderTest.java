package ru.romangr.simplestatemachine;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import ru.romangr.simplestatemachine.StateMachineTest.TwoEvents;
import ru.romangr.simplestatemachine.StateMachineTest.ThreeStates;

class StateMachineBuilderTest {

  @Test
  void duplicatedTransitionsAreNotAllowed() {
    assertThatThrownBy(() ->
        StateMachine.<ThreeStates, TwoEvents>builder()
            .withStates(ThreeStates.class)
            .withEvents(TwoEvents.class)
            .withInitialState(ThreeStates.ONE)
            .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)
            .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)
            .build())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Duplicated transitions are not allowed!");
  }

  @Test
  void ambiguousTransitionsAreNotAllowed() {
    assertThatThrownBy(() ->
        StateMachine.<ThreeStates, TwoEvents>builder()
            .withStates(ThreeStates.class)
            .withEvents(TwoEvents.class)
            .withInitialState(ThreeStates.ONE)
            .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)
            .withTransition(ThreeStates.ONE, ThreeStates.THREE, TwoEvents.UP)
            .build())
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Ambiguous transitions: ")
        .hasMessageContaining("'{ ONE ---UP--> THREE }'")
        .hasMessageContaining("and")
        .hasMessageContaining("'{ ONE ---UP--> TWO }'");
  }
}
