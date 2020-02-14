package ru.romangr.simplestatemachine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class StateMachineTest {

  @Test
  void buildStateMachine() {
    StateMachine<ThreeStates, TwoEvents> stateMachine = StateMachine.<ThreeStates, TwoEvents>builder()
        .withStates(ThreeStates.class)
        .withEvents(TwoEvents.class)
        .withInitialState(ThreeStates.ONE)
        .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)
        .withTransition(ThreeStates.TWO, ThreeStates.ONE, TwoEvents.DOWN)
        .build();

    assertThat(stateMachine).isNotNull();
  }

  @Test
  void initialState() {
    StateMachine<ThreeStates, TwoEvents> stateMachine = StateMachine.<ThreeStates, TwoEvents>builder()
        .withStates(ThreeStates.class)
        .withEvents(TwoEvents.class)
        .withInitialState(ThreeStates.ONE)
        .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)
        .withTransition(ThreeStates.TWO, ThreeStates.ONE, TwoEvents.DOWN)
        .withTransition(ThreeStates.TWO, ThreeStates.THREE, TwoEvents.UP)
        .withTransition(ThreeStates.THREE, ThreeStates.TWO, TwoEvents.DOWN)
        .build();

    assertThat(stateMachine.currentState()).isEqualTo(ThreeStates.ONE);
  }

  @Test
  void transitions() {
    StateMachine<ThreeStates, TwoEvents> stateMachine = StateMachine.<ThreeStates, TwoEvents>builder()
        .withStates(ThreeStates.class)
        .withEvents(TwoEvents.class)
        .withInitialState(ThreeStates.ONE)
        .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)
        .withTransition(ThreeStates.TWO, ThreeStates.ONE, TwoEvents.DOWN)
        .withTransition(ThreeStates.TWO, ThreeStates.THREE, TwoEvents.UP)
        .withTransition(ThreeStates.THREE, ThreeStates.TWO, TwoEvents.DOWN)
        .build();

    EventAcceptResult<ThreeStates> result1 = stateMachine.acceptEvent(TwoEvents.UP);
    assertThat(result1.newState()).isEqualTo(ThreeStates.TWO);
    assertThat(result1.result()).isEqualTo(EventAcceptStatus.SUCCESS);

    EventAcceptResult<ThreeStates> result2 = stateMachine.acceptEvent(TwoEvents.UP);
    assertThat(result2.newState()).isEqualTo(ThreeStates.THREE);
    assertThat(result2.result()).isEqualTo(EventAcceptStatus.SUCCESS);
  }

  @Test
  void reset() {
    StateMachine<ThreeStates, TwoEvents> stateMachine = StateMachine.<ThreeStates, TwoEvents>builder()
        .withStates(ThreeStates.class)
        .withEvents(TwoEvents.class)
        .withInitialState(ThreeStates.ONE)
        .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)
        .withTransition(ThreeStates.TWO, ThreeStates.ONE, TwoEvents.DOWN)
        .withTransition(ThreeStates.TWO, ThreeStates.THREE, TwoEvents.UP)
        .withTransition(ThreeStates.THREE, ThreeStates.TWO, TwoEvents.DOWN)
        .build();

    EventAcceptResult<ThreeStates> result1 = stateMachine.acceptEvent(TwoEvents.UP);
    assertThat(result1.newState()).isEqualTo(ThreeStates.TWO);
    assertThat(result1.result()).isEqualTo(EventAcceptStatus.SUCCESS);

    stateMachine.reset();

    assertThat(stateMachine.currentState()).isEqualTo(ThreeStates.ONE);
  }

  @Test
  void circularTransitions() {
    StateMachine<ThreeStates, TwoEvents> stateMachine = StateMachine.<ThreeStates, TwoEvents>builder()
        .withStates(ThreeStates.class)
        .withEvents(TwoEvents.class)
        .withInitialState(ThreeStates.ONE)
        .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)
        .withTransition(ThreeStates.TWO, ThreeStates.THREE, TwoEvents.UP)
        .withTransition(ThreeStates.THREE, ThreeStates.ONE, TwoEvents.UP)
        .build();

    EventAcceptResult<ThreeStates> result1 = stateMachine.acceptEvent(TwoEvents.UP);
    assertThat(result1.newState()).isEqualTo(ThreeStates.TWO);
    assertThat(result1.result()).isEqualTo(EventAcceptStatus.SUCCESS);

    EventAcceptResult<ThreeStates> result2 = stateMachine.acceptEvent(TwoEvents.UP);
    assertThat(result2.newState()).isEqualTo(ThreeStates.THREE);
    assertThat(result2.result()).isEqualTo(EventAcceptStatus.SUCCESS);

    EventAcceptResult<ThreeStates> result3 = stateMachine.acceptEvent(TwoEvents.UP);
    assertThat(result3.newState()).isEqualTo(ThreeStates.ONE);
    assertThat(result3.result()).isEqualTo(EventAcceptStatus.SUCCESS);
  }

  @Test
  void unexpectedEvent() {
    StateMachine<ThreeStates, TwoEvents> stateMachine = StateMachine.<ThreeStates, TwoEvents>builder()
        .withStates(ThreeStates.class)
        .withEvents(TwoEvents.class)
        .withInitialState(ThreeStates.ONE)
        .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)
        .withTransition(ThreeStates.TWO, ThreeStates.ONE, TwoEvents.DOWN)
        .withTransition(ThreeStates.TWO, ThreeStates.THREE, TwoEvents.UP)
        .withTransition(ThreeStates.THREE, ThreeStates.TWO, TwoEvents.DOWN)
        .build();

    EventAcceptResult<ThreeStates> result1 = stateMachine.acceptEvent(TwoEvents.DOWN);
    assertThat(result1.newState()).isEqualTo(ThreeStates.ONE);
    assertThat(result1.result()).isEqualTo(EventAcceptStatus.UNEXPECTED_EVENT);
  }

  @Test
  void stateWithNoTransitionsFrom() {
    StateMachine<ThreeStates, TwoEvents> stateMachine = StateMachine.<ThreeStates, TwoEvents>builder()
        .withStates(ThreeStates.class)
        .withEvents(TwoEvents.class)
        .withInitialState(ThreeStates.ONE)
        .withTransition(ThreeStates.TWO, ThreeStates.THREE, TwoEvents.UP)
        .withTransition(ThreeStates.THREE, ThreeStates.ONE, TwoEvents.DOWN)
        .build();

    EventAcceptResult<ThreeStates> result1 = stateMachine.acceptEvent(TwoEvents.UP);
    assertThat(result1.newState()).isEqualTo(ThreeStates.ONE);
    assertThat(result1.result()).isEqualTo(EventAcceptStatus.UNEXPECTED_EVENT);
  }

  @Test
  void restoreFromMapWithCurrentStateNotEqualToInitialState() {
    Map<ThreeStates, Map<TwoEvents, Transition<ThreeStates, TwoEvents>>> transitionsMap =
        StateMachine.<ThreeStates, TwoEvents>builder()
            .withStates(ThreeStates.class)
            .withEvents(TwoEvents.class)
            .withInitialState(ThreeStates.ONE)
            .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)
            .withTransition(ThreeStates.TWO, ThreeStates.ONE, TwoEvents.DOWN)
            .withTransition(ThreeStates.TWO, ThreeStates.THREE, TwoEvents.UP)
            .withTransition(ThreeStates.THREE, ThreeStates.TWO, TwoEvents.DOWN)
            .asTransitionsMap();

    StateMachine<ThreeStates, TwoEvents> stateMachine = new StateMachine<>(
        ThreeStates.ONE, ThreeStates.TWO, transitionsMap);

    assertThat(stateMachine.currentState()).isEqualTo(ThreeStates.TWO);
  }

}
