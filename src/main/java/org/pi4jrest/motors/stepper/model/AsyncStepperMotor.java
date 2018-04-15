package org.pi4jrest.motors.stepper.model;

import org.pi4jrest.motors.stepper.model.states.StepperMotorStopState;

import java.util.concurrent.Future;

public interface AsyncStepperMotor extends StepperMotor {

    Future<StepperMotorStopState> asyncStep(int stepCount);

    Future<StepperMotorStopState> asyncStep(int stepCount, StepType stepType, int stepInterval);
}
