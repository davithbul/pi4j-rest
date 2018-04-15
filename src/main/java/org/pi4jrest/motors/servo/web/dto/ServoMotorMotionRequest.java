package org.pi4jrest.motors.servo.web.dto;

public class ServoMotorMotionRequest {
    private int degree;
    private int speed;

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
