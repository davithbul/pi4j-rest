package org.pi4jrest.motors.stepper.services;

public class StepperMotorMoveFactory {

    public static StepperMotorMove getMove(String name, int stepCount) {
        switch (name.toLowerCase()) {
            case "step":
                return step(stepCount);
            case "rotate":
                return rotate(stepCount);
            case "forward":
                return forward(stepCount);
            case "reverse":
                return reverse(stepCount);
            default:
                throw new RuntimeException("Can't find move type: " + name);
        }
    }

    /**
     * Motor moves {@code revolutionCount} steps.
     * Moves clockWise if step count is positive,
     * and reverse otherwise.
     */
    public static StepperMotorMove step(int stepCount) {
        return motorComponent -> motorComponent.step(stepCount);
    }

    /**
     * Motor rotates {@code revolutionCount} revolution.
     * Rotates clockWise if revolution count is positive,
     * and reverse otherwise.
     */
    public static StepperMotorMove rotate(int revolutionCount) {
        return motorComponent -> motorComponent.rotate(revolutionCount);
    }

    /**
     * Motor goes forward for given {@code milliseconds} milliseconds.
     */
    public static StepperMotorMove forward(int milliseconds) {
        return motorComponent -> motorComponent.forward(milliseconds);
    }

    /**
     * Motor goes reverse for given {@code milliseconds} milliseconds.
     */
    public static StepperMotorMove reverse(int milliseconds) {
        return motorComponent -> motorComponent.reverse(milliseconds);
    }
}
