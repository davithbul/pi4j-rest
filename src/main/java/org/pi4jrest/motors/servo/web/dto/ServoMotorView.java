package org.pi4jrest.motors.servo.web.dto;

import org.pi4jrest.motors.model.MotorState;
import org.pi4jrest.motors.servo.model.ServoMotorConnection;
import org.pi4jrest.pins.model.GpioPinDescription;

public class ServoMotorView {
    private final String name;
    private final GpioPinDescription pin;
    private final MotorState state;

    public ServoMotorView(ServoMotorConnection servoMotorConnection, MotorState state) {
        this.name = servoMotorConnection.getName();
        this.pin = servoMotorConnection.getPin();
        this.state = state;
    }

    public MotorState getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public GpioPinDescription getPin() {
        return pin;
    }
}
