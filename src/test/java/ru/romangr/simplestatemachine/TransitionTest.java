package ru.romangr.simplestatemachine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TransitionTest {

  @ParameterizedTest(name = "equalsTest: {0}")
  @MethodSource("equalsTestData")
  void equalsTest(@SuppressWarnings("unused") String name, Object transition1, Object transition2,
      boolean expectedResult) {
    assertThat(transition1.equals(transition2)).isEqualTo(expectedResult);
  }

  private static Stream<Arguments> equalsTestData() {
    Object transition = new Transition<>(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP);
    return Stream.of(
        arguments(
            "equal transitions",
            new Transition<>(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP),
            new Transition<>(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP),
            true
        ),
        arguments(
            "different fromState",
            new Transition<>(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP),
            new Transition<>(ThreeStates.THREE, ThreeStates.TWO, TwoEvents.UP),
            false
        ),
        arguments(
            "different toState",
            new Transition<>(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP),
            new Transition<>(ThreeStates.ONE, ThreeStates.THREE, TwoEvents.UP),
            false
        ),
        arguments(
            "different events",
            new Transition<>(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP),
            new Transition<>(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.DOWN),
            false
        ),
        arguments(
            "null transition",
            new Transition<>(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP),
            null,
            false
        ),
        arguments(
            "not a transition",
            new Transition<>(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP),
            "test",
            false
        ),
        arguments(
            "the same transition",
            transition,
            transition,
            true
        )
    );
  }
}
