package org.pi4jrest.motors.servo.model;

import org.pi4jrest.motors.exceptions.MotorStoppingException;
import org.pi4jrest.motors.model.Motor;
import org.pi4jrest.motors.model.MotorState;

/**
 * {@link ServoMotor} object represents basic servo motor object.
 * It's possible to control current servo motor by rotating it.
 * It is also possible to get current position of the servo motor.
 */
public interface ServoMotor extends Motor {

    /**
     * represents degree on which servo motor should be rotated.
     * Degree should be between -90 to 90. 0 degree is a neutral position
     * -90 is a counter clockwise max point and correspondingly 90 is a
     * max point counted clockwise.
     *
     * @param degree      number between [-90; 90]
     * @param movingSpeed the lower number, the faster move
     * @throws InterruptedException thrown when interruption get issued during execution.
     */
    void rotate(int degree, int movingSpeed) throws InterruptedException;

    /**
     * Stops currently running motor.
     *
     * @throws MotorStoppingException when it can not stop motor.
     */
    void stop() throws MotorStoppingException;

    MotorState getCurrentState();
}
