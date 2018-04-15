package org.pi4jrest.motors.stepper.web.responses;

import org.pi4jrest.motors.stepper.model.StepperMotorConnection;
import org.pi4jrest.motors.stepper.model.StepperMotorState;
import org.pi4jrest.pins.model.GpioPinDescription;

public class StepperMotorView {
    private final StepperMotorState state;
    private final String name;
    private final GpioPinDescription pin1;
    private final GpioPinDescription pin2;
    private final GpioPinDescription pin3;
    private final GpioPinDescription pin4;

    public StepperMotorView(StepperMotorConnection motorDescription, StepperMotorState state) {
        this.name = motorDescription.getName();
        this.pin1 = motorDescription.getPin1();
        this.pin2 = motorDescription.getPin2();
        this.pin3 = motorDescription.getPin3();
        this.pin4 = motorDescription.getPin4();
        this.state = state;
    }

    public StepperMotorState getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public GpioPinDescription getPin1() {
        return pin1;
    }

    public GpioPinDescription getPin2() {
        return pin2;
    }

    public GpioPinDescription getPin3() {
        return pin3;
    }

    public GpioPinDescription getPin4() {
        return pin4;
    }
}
