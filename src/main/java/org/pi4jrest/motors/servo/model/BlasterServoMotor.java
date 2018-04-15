package org.pi4jrest.motors.servo.model;

import com.pi4j.component.servo.ServoDriver;
import org.pi4jrest.motors.model.MotorState;
import org.pi4jrest.motors.servo.exceptions.InvalidRotationDegreeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlasterServoMotor implements ServoMotor {

    private final static Logger log = LoggerFactory.getLogger(BlasterServoMotor.class);

    private ServoDriver servoDriver;

    public BlasterServoMotor(ServoDriver servoDriver) {
        this.servoDriver = servoDriver;
    }

    @Override
    public void rotate(int degree, int speed) throws InterruptedException {
        log.info("Rotating servo motor to {} degree.", degree);

        if (degree < -90 || degree > 90) {
            throw new InvalidRotationDegreeException(degree);
        }

        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < 120000) { // 2 minutes
            log.info("Run process again");
            for (int i = 50; i < 150; i++) {
                log.info("+Set servo pulse width: {}", i);
                servoDriver.setServoPulseWidth(i); // Set raw value for this servo driver - 50 to 195
                Thread.sleep(10);
            }

            for (int i = 150; i > 50; i--) {
                log.info("-Set servo pulse width: {}", i);
                servoDriver.setServoPulseWidth(i); // Set raw value for this servo driver - 50 to 195
                Thread.sleep(10);
            }
        }
        log.info("Exiting RPIServoBlasterExample");
    }

    @Override
    public void stop() {
    }

    @Override
    public MotorState getCurrentState() {
        return ServoMotorStopState.stopped();
    }
}
