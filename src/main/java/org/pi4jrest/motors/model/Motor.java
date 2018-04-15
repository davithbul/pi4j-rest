package org.pi4jrest.motors.model;

import org.pi4jrest.motors.exceptions.MotorStoppingException;

/**
 * Represents physical motor, with default motor functions.
 */
public interface Motor {

    void stop() throws MotorStoppingException;

    MotorState getCurrentState();
}
