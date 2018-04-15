package org.pi4jrest.motors.servo.factory;

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.io.gpio.Pin;

import java.io.IOException;

public class LocalServoBlasterServoDriver implements ServoDriver {

    protected Pin servoPin;
    protected int index;
    protected String pinString;
    protected int servoPosition;
    protected LocalServoBlasterProvider provider;

    protected LocalServoBlasterServoDriver(Pin servoPin, int index, String pinString, LocalServoBlasterProvider provider) throws IOException {
        this.index = index;
        this.servoPin = servoPin;
        this.pinString = pinString;
        this.provider = provider;
    }


    public int getServoPulseWidth() {
        return servoPosition;
    }

    public void setServoPulseWidth(int width) {
        this.servoPosition = width;
        provider.updateServo(pinString, width);
    }

    public int getServoPulseResolution() {
        return 100;
    }

    @Override
    public Pin getPin() {
        return servoPin;
    }
}
