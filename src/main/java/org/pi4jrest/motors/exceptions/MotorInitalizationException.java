package org.pi4jrest.motors.exceptions;

public class MotorInitalizationException extends RuntimeException {

    public MotorInitalizationException() {
    }

    public MotorInitalizationException(String message) {
        super(message);
    }

    public MotorInitalizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MotorInitalizationException(Throwable cause) {
        super(cause);
    }
}
