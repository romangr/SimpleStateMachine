# SimpleStateMachine ![Build](https://github.com/romangr/SimpleStateMachine/workflows/Build/badge.svg) [![Publish to Packages](https://github.com/romangr/SimpleStateMachine/actions/workflows/publish.yml/badge.svg)](https://github.com/romangr/SimpleStateMachine/actions/workflows/publish.yml)

SimpleStateMachine is a small util to build small state machines in Java

## Download
The library is available on JCenter public repository

### Gradle
```groovy
implementation 'ru.romangr:simple-state-machine:0.0.3'
```

### Maven
```xml
<dependency>
	<groupId>ru.romangr</groupId>
	<artifactId>simple-state-machine</artifactId>
	<version>0.0.3</version>
	<type>pom</type>
</dependency>
```


## Usage

Declare states as a Java enum
```java
enum ThreeStates {
  ONE,
  TWO,
  THREE
}
```

Declare events as a Java enum
```java
enum TwoEvents {
  UP,
  DOWN
}
```

Build your state machine
```java
    StateMachine<ThreeStates, TwoEvents> stateMachine = StateMachine.<ThreeStates, TwoEvents>builder()
        .withStates(ThreeStates.class)                                      // specify a list valid states
        .withEvents(TwoEvents.class)                                        // specify a list valid events
        .withInitialState(ThreeStates.ONE) // specify initial state         // specify an initial state
        .withTransition(ThreeStates.ONE, ThreeStates.TWO, TwoEvents.UP)     // declare some valid transitions
        .withTransition(ThreeStates.TWO, ThreeStates.ONE, TwoEvents.DOWN)
        .build();
```

Pass some event to the state machine
```java
EventAcceptResult<ThreeStates> result = stateMachine.acceptEvent(TwoEvents.UP);
```

The result of this call consists of a new state and a result of passing the event. Possible results are declared in EventAcceptStatus enum
```java
public enum EventAcceptStatus {
  SUCCESS,            // event has been successfully accepted and state machine have made a transition
  UNEXPECTED_EVENT    // event is not valid for the current state of the state machine, the state hasn't changed
}
```
