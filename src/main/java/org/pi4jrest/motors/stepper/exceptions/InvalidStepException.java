package org.pi4jrest.motors.stepper.exceptions;

public class InvalidStepException extends RuntimeException {

    private int step;

    public InvalidStepException(int step) {
        this.step = step;
    }

    public InvalidStepException(int step, String message) {
        super(message);
        this.step = step;
    }

    public int getStep() {
        return step;
    }
}
