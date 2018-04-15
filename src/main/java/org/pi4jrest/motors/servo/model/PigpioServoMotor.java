package org.pi4jrest.motors.servo.model;


import org.pi4jrest.motors.exceptions.MotorStoppingException;
import org.pi4jrest.motors.model.MotorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.pigpioj.PigpioInterface;
import uk.pigpioj.PigpioSocket;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PigpioServoMotor implements ServoMotor {

    private final static Logger log = LoggerFactory.getLogger(PigpioServoMotor.class);

    private static final int MIN_POSITION_PULSE_WIDTH = 600;
    private static final int MAX_POSITION_PULSE_WIDTH = 2500;
    private static final int MID_POSITION_PULSE_WIDTH = 1550;
    private static PigpioInterface pigpioInterface;

    static {
        //pigpioInterface = PigpioJ.getImplementation()
        PigpioSocket pigpiod = new PigpioSocket();
        try {
            pigpiod.connect("localhost", 8888);
            pigpioInterface = pigpiod;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private final Object stopLock = new Object();
    private final int pin;
    private Integer currentPulseWidth;
    private AtomicReference<MotorState> motorState = new AtomicReference<>(ServoMotorStopState.stopped());
    private AtomicBoolean stopRequested = new AtomicBoolean(false);

    public PigpioServoMotor(int pin) {
        this.pin = pin;
    }

    @Override
    public synchronized void rotate(int degree, int speed) throws InterruptedException {
        try {
            motorState.set(new ServoMotorRunningState(degree, LocalDateTime.now()));

            pigpioInterface.setPWMFrequency(pin, 50);
            pigpioInterface.setPWMRange(pin, pigpioInterface.getPWMRealRange(pin));

            if (currentPulseWidth == null) {
                currentPulseWidth = MID_POSITION_PULSE_WIDTH;
                pigpioInterface.setServoPulseWidth(pin, currentPulseWidth);
            }

            int nextPulseWidth = convertDegree(MAX_POSITION_PULSE_WIDTH, MIN_POSITION_PULSE_WIDTH, degree);
            log.debug("Moving from current position {} to new position {}.", currentPulseWidth, nextPulseWidth);

            int sign = nextPulseWidth > currentPulseWidth ? 1 : -1;
            for (; currentPulseWidth != nextPulseWidth; currentPulseWidth += sign) {
                if (stopRequested.get()) {
                    throw new InterruptedException();
                }
                pigpioInterface.setServoPulseWidth(pin, currentPulseWidth);
                Thread.sleep(speed);
            }
        } finally {
            motorState.set(ServoMotorStopState.stopped());
        }
    }

    private int convertDegree(int max, int min, int degree) {
        return (int) (degree * ((float) max - min) / 180 + MID_POSITION_PULSE_WIDTH);
    }

    @Override
    public void stop() throws MotorStoppingException {
        synchronized (stopLock) {
            stopRequested.set(true);
            try {
                while (motorState.get() != ServoMotorStopState.stopped()) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new MotorStoppingException(e);
                    }
                }
            } finally {
                stopRequested.set(false);
            }
        }
    }

    @Override
    public MotorState getCurrentState() {
        return motorState.get();
    }
}
