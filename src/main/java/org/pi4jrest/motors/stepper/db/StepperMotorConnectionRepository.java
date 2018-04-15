package org.pi4jrest.motors.stepper.db;

import org.pi4jrest.motors.stepper.model.StepperMotorConnection;

import java.util.Collection;
import java.util.Optional;

public interface StepperMotorConnectionRepository {

    Optional<StepperMotorConnection> get(String name);

    StepperMotorConnection save(StepperMotorConnection stepperMotor);

    void delete(String name);

    Collection<StepperMotorConnection> list();
}
