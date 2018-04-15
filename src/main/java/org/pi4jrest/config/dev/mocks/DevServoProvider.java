package org.pi4jrest.config.dev.mocks;

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.component.servo.ServoProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DevServoProvider implements ServoProvider {

    public DevServoProvider() throws IOException {
    }

    @Override
    public List<Pin> getDefinedServoPins() throws IOException {
        return Arrays.asList(RaspiPin.allPins());
    }

    @Override
    public synchronized ServoDriver getServoDriver(Pin servoPin) throws IOException {
        return new ServoDriver() {
            @Override
            public int getServoPulseWidth() {
                return 0;
            }

            @Override
            public void setServoPulseWidth(int width) {

            }

            @Override
            public int getServoPulseResolution() {
                return 0;
            }

            @Override
            public Pin getPin() {
                return servoPin;
            }
        };
    }
}
