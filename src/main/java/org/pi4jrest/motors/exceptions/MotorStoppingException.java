package org.pi4jrest.motors.exceptions;

public class MotorStoppingException extends Exception {

    public MotorStoppingException(String message) {
        super(message);
    }

    public MotorStoppingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MotorStoppingException(Throwable cause) {
        super(cause);
    }
}
