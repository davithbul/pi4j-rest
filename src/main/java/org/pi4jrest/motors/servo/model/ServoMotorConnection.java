package org.pi4jrest.motors.servo.model;

import org.pi4jrest.pins.model.GpioPinDescription;

import java.io.Serializable;

public class ServoMotorConnection implements Serializable {
    private String name;
    private GpioPinDescription pin;

    public ServoMotorConnection(String name, GpioPinDescription pin) {
        this.name = name;
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public GpioPinDescription getPin() {
        return pin;
    }
}
