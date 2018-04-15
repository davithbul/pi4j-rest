package org.pi4jrest.motors.stepper.events;

public class StepperMotorUpdateEvent {

    private String name;

    public StepperMotorUpdateEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
