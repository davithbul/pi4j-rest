package org.pi4jrest.motors.servo.exceptions;

public class InvalidRotationDegreeException extends RuntimeException {
    private int requestedDegree;

    public InvalidRotationDegreeException(int requestedDegree) {
        super("Invalid degree has been requested to rotate servo motor: " + requestedDegree);
        this.requestedDegree = requestedDegree;
    }

    public int getRequestedDegree() {
        return requestedDegree;
    }
}
