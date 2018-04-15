package org.pi4jrest.motors.stepper.web.requests;

import org.pi4jrest.pins.model.GpioPinDescription;

public class StepperMotorRequest {
    private GpioPinDescription pin1;
    private GpioPinDescription pin2;
    private GpioPinDescription pin3;
    private GpioPinDescription pin4;

    public GpioPinDescription getPin1() {
        return pin1;
    }

    public void setPin1(GpioPinDescription pin1) {
        this.pin1 = pin1;
    }

    public GpioPinDescription getPin2() {
        return pin2;
    }

    public void setPin2(GpioPinDescription pin2) {
        this.pin2 = pin2;
    }

    public GpioPinDescription getPin3() {
        return pin3;
    }

    public void setPin3(GpioPinDescription pin3) {
        this.pin3 = pin3;
    }

    public GpioPinDescription getPin4() {
        return pin4;
    }

    public void setPin4(GpioPinDescription pin4) {
        this.pin4 = pin4;
    }
}
