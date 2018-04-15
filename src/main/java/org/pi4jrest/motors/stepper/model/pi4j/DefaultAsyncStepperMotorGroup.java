package org.pi4jrest.motors.stepper.model.pi4j;

import org.pi4jrest.motors.stepper.model.AsyncStepperMotor;
import org.pi4jrest.motors.stepper.model.StepType;
import org.pi4jrest.motors.stepper.model.adapters.AdaptedStepperMotorComponent;
import org.pi4jrest.motors.stepper.model.states.StepperMotorStopState;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DefaultAsyncStepperMotorGroup extends StepperMotorGroup implements AsyncStepperMotor {

    private ExecutorService executorService;

    public DefaultAsyncStepperMotorGroup(Collection<AdaptedStepperMotorComponent> stepperMotorComponents, ExecutorService executorService) {
        super(stepperMotorComponents);
        this.executorService = executorService;
    }

    @Override
    public Future<StepperMotorStopState> asyncStep(int stepCount) {
        return executorService.submit(() -> {
            try {
                step(stepCount);
            } catch (InterruptedException e) {
                //it's a async execution, so no way to convey interruption
            }
            return StepperMotorStopState.stopped();
        });
    }

    @Override
    public Future<StepperMotorStopState> asyncStep(int stepCount, StepType stepType, int stepInterval) {
        return executorService.submit(() -> {
            try {
                step(stepCount, stepType, stepInterval);
            } catch (InterruptedException e) {
                //it's a async execution, so no way to convey interruption
            }
            return StepperMotorStopState.stopped();
        });
    }
}
