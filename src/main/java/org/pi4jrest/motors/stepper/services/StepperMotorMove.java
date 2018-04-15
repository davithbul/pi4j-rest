package org.pi4jrest.motors.stepper.services;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;

@FunctionalInterface
public interface StepperMotorMove {
    void accept(GpioStepperMotorComponent motorComponent);
}
