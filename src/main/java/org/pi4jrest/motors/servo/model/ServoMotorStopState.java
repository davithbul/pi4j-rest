package org.pi4jrest.motors.servo.model;

import org.pi4jrest.motors.model.MotorState;

public class ServoMotorStopState implements MotorState {

    private static ServoMotorStopState STOPPED = new ServoMotorStopState();

    private ServoMotorStopState() {
    }

    public static ServoMotorStopState stopped() {
        return STOPPED;
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
