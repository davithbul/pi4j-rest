package org.pi4jrest.motors.stepper.events;


public class StepperMotorDeleteEvent {

    private String name;

    public StepperMotorDeleteEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
