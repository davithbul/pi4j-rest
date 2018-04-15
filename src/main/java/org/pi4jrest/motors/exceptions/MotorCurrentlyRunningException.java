package org.pi4jrest.motors.exceptions;

import org.pi4jrest.motors.model.MotorState;

public class MotorCurrentlyRunningException extends MotorExecutionException {

    private MotorState motorState;

    public MotorCurrentlyRunningException(MotorState motorState) {
        super("Motor currently moving, please stop first!");
        this.motorState = motorState;
    }

    public MotorCurrentlyRunningException(String name, MotorState motorState) {
        super("Motor " + name + " currently moving, please stop first!");
        this.motorState = motorState;
    }

    public MotorState getMotorState() {
        return motorState;
    }
}
