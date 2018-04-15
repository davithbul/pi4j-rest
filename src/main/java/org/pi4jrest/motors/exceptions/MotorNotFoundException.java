package org.pi4jrest.motors.exceptions;

public class MotorNotFoundException extends RuntimeException {
    private String name;

    public MotorNotFoundException(String name) {
        super("Can not find stepper motor with name: " + name);
    }

    public String getName() {
        return name;
    }
}
