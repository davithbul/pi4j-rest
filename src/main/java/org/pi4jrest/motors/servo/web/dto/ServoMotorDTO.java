package org.pi4jrest.motors.servo.web.dto;

import org.pi4jrest.pins.model.GpioPinDescription;

public class ServoMotorDTO {
    private GpioPinDescription pin;

    public GpioPinDescription getPin() {
        return pin;
    }

    public void setPin(GpioPinDescription pin) {
        this.pin = pin;
    }
}
