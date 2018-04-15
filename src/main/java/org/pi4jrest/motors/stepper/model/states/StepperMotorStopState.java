package org.pi4jrest.motors.stepper.model.states;

import org.pi4jrest.motors.stepper.model.StepperMotorState;

public class StepperMotorStopState implements StepperMotorState {

    private static StepperMotorStopState STOPPED = new StepperMotorStopState();

    private StepperMotorStopState() {
    }

    public static StepperMotorStopState stopped() {
        return STOPPED;
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
