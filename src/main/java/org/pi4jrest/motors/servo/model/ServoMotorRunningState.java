package org.pi4jrest.motors.servo.model;

import org.pi4jrest.motors.stepper.model.StepperMotorState;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ServoMotorRunningState implements StepperMotorState {

    private final int degree;
    private final LocalDateTime startTime;

    public ServoMotorRunningState(int degree) {
        this(degree, LocalDateTime.now());
    }

    public ServoMotorRunningState(int degree, LocalDateTime startTime) {
        this.degree = degree;
        this.startTime = startTime;
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    public int getDegree() {
        return degree;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getRunningTimeInSeconds() {
        return startTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
    }
}
