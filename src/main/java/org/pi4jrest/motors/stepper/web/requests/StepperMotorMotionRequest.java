package org.pi4jrest.motors.stepper.web.requests;

import org.pi4jrest.motors.stepper.model.StepType;

public class StepperMotorMotionRequest {
    private String motion;
    private StepType stepType;
    private int stepInterval;
    private int count;

    public String getMotion() {
        return motion;
    }

    public void setMotion(String motion) {
        this.motion = motion;
    }

    public StepType getStepType() {
        return stepType;
    }

    public void setStepType(StepType stepType) {
        this.stepType = stepType;
    }

    public int getStepInterval() {
        return stepInterval;
    }

    public void setStepInterval(int stepInterval) {
        this.stepInterval = stepInterval;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
