package org.pi4jrest.motors.stepper.model.states;

import org.pi4jrest.motors.stepper.model.StepDirection;
import org.pi4jrest.motors.stepper.model.StepperMotorState;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class StepperMotorRunningState implements StepperMotorState {

    private final StepDirection direction;
    private final int requestedStepCount;
    private final LocalDateTime startTime;
    private int executedStepCount;

    public StepperMotorRunningState(StepDirection direction, int requestedStepCount) {
        this(direction, requestedStepCount, 0, LocalDateTime.now());
    }

    public StepperMotorRunningState(StepDirection direction, int requestedStepCount, int executedStepCount, LocalDateTime startTime) {
        this.direction = direction;
        this.requestedStepCount = requestedStepCount;
        this.executedStepCount = executedStepCount;
        this.startTime = startTime;
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    public StepDirection getDirection() {
        return direction;
    }

    public void incrementExecutedStepCount() {
        executedStepCount++;
    }

    public int getExecutedStepCount() {
        return executedStepCount;
    }

    public int getRequestedStepCount() {
        return requestedStepCount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getRunningTimeInSeconds() {
        return startTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
    }
}
