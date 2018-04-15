package org.pi4jrest.motors.exceptions;

public class MotorExecutionException extends RuntimeException {

    public MotorExecutionException() {
    }

    public MotorExecutionException(String message) {
        super(message);
    }

    public MotorExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MotorExecutionException(Throwable cause) {
        super(cause);
    }
}
