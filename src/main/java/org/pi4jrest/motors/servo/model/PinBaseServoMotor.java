package org.pi4jrest.motors.servo.model;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.pi4jrest.motors.exceptions.MotorStoppingException;
import org.pi4jrest.motors.model.MotorState;
import org.pi4jrest.motors.servo.exceptions.InvalidRotationDegreeException;
import org.pi4jrest.motors.stepper.model.states.StepperMotorStopState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PinBaseServoMotor implements ServoMotor {

    private final static Logger log = LoggerFactory.getLogger(PinBaseServoMotor.class);

    private final AtomicReference<MotorState> currentState = new AtomicReference<>(StepperMotorStopState.stopped());
    private final AtomicBoolean stopSignalIssued = new AtomicBoolean(false);
    private GpioPinDigitalOutput servoPin;

    public PinBaseServoMotor(GpioPinDigitalOutput servoPin) {
        this.servoPin = servoPin;
    }

    @Override
    public synchronized void rotate(int degree, int speed) throws InterruptedException {
        log.info("Rotating servo motor to {} degree.", degree);

        if (degree < -90 || degree > 90) {
            throw new InvalidRotationDegreeException(degree);
        }

        try {
            stop();
        } catch (MotorStoppingException e) {
            throw new RuntimeException(e.getCause());
        }

        stopSignalIssued.set(false);
        rotateAsync(degree);
    }

    @Override
    public void stop() throws MotorStoppingException {
        stopSignalIssued.set(true);
        while (currentState.get().isRunning()) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new MotorStoppingException(e);
            }
        }
    }

    @Async
    protected synchronized void rotateAsync(int degree) throws InterruptedException {
        try {
            currentState.set(new ServoMotorRunningState(degree));

            //highTime [0-2] = degree [-90;90]
            float highTime = (degree + 90) / 90f;
            while (!stopSignalIssued.get()) {
                float lowTime = 1000 / 50 - highTime;
                servoPin.high();
                long upMs = new Float(highTime).longValue(); // Up time miliseconds
                int upNanos = new Float(highTime * 1000000 % 1000000).intValue(); // Up time nanoseconds
                java.lang.Thread.sleep(upMs, upNanos);

                servoPin.low();
                long lowMs = new Float(lowTime).longValue();
                int lowNanos = new Float(lowTime * 1000000 % 1000000).intValue();

                java.lang.Thread.sleep(lowMs, lowNanos);
            }
        } finally {
            currentState.set(ServoMotorStopState.stopped());
        }
    }

    @Override
    public MotorState getCurrentState() {
        return currentState.get();
    }
}
