package org.pi4jrest.motors.stepper.model.pi4j;

import com.google.common.base.Preconditions;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.pi4jrest.motors.exceptions.MotorCurrentlyRunningException;
import org.pi4jrest.motors.stepper.exceptions.InvalidStepException;
import org.pi4jrest.motors.stepper.model.StepDirection;
import org.pi4jrest.motors.stepper.model.StepType;
import org.pi4jrest.motors.stepper.model.StepperMotor;
import org.pi4jrest.motors.stepper.model.StepperMotorState;
import org.pi4jrest.motors.stepper.model.adapters.AdaptedStepperMotorComponent;
import org.pi4jrest.motors.stepper.model.states.StepperMotorRunningState;
import org.pi4jrest.motors.stepper.model.states.StepperMotorStopState;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Default stepper motor implementation, which can receive
 * basic stepping and stopping requests. Stepper motor step request will
 * fail if there is already step request executing.
 * Consider using StepperMotorChainStepping class for chaining multiple steps
 * one after another.
 */
public class DefaultStepperMotor implements StepperMotor {

    /**
     * Represents current state of stepper motor.
     */
    private final AtomicReference<StepperMotorState> currentState = new AtomicReference<>(StepperMotorStopState.stopped());

    private final AdaptedStepperMotorComponent motorComponent;
    private final Lock lock = new ReentrantLock();
    private final Condition stoppedRunning = lock.newCondition();
    /**
     * Flag which shows if stop get requested.
     */
    private AtomicBoolean stopRequested = new AtomicBoolean(false);

    public DefaultStepperMotor(GpioPinDigitalOutput[] pins) {
        Preconditions.checkArgument(pins.length == 4, "Stepper motor needs 4 pins.");

        // create motor component
        this.motorComponent = new AdaptedStepperMotorComponent(new GpioPinDigitalOutput[]{
                pins[3], pins[1], pins[0], pins[2]
        });

        // define stepper parameters before attempting to control motor
        // anything lower than 2 ms does not work for my sample motor using single step sequence
        motorComponent.setStepInterval(1);
        motorComponent.setStepSequence(StepType.SINGLE_STEP.getStepSequence());

        // There are 32 steps per revolution on my sample motor, and inside is a ~1/64 reduction gear set.
        // Gear reduction is actually: (32/9)/(22/11)x(26/9)x(31/10)=63.683950617
        // This means is that there are really 32*63.683950617 steps per revolution =  2037.88641975 ~ 2038 steps!
        motorComponent.setStepsPerRevolution(2038);
    }

    @Override
    public void step(int stepCount) throws InterruptedException {
        step(stepCount, StepType.SINGLE_STEP, 1500000);
    }

    @Override
    public void step(int stepCount, StepType stepType, int stepInterval) throws InterruptedException {
        if (stepCount == 0) {
            throw new InvalidStepException(0,
                    "Can not step 0 steps, please use stop for stopping stepper motor.");
        }

        if (currentState.get().isRunning()) {
            throw new MotorCurrentlyRunningException(currentState.get());
        }

        motorComponent.setStepInterval(NANOSECONDS.toMillis(stepInterval), stepInterval % 1000000);

        StepDirection direction = stepCount > 0 ? StepDirection.CLOCK_WISE : StepDirection.COUNTER_CLOCK_WISE;
        StepperMotorRunningState newState = new StepperMotorRunningState(
                direction,
                Math.abs(stepCount));

        boolean successfullyUpdated = currentState.compareAndSet(StepperMotorStopState.stopped(), newState);

        //if there is concurrent request, one might start moving before another one
        if (!successfullyUpdated) {
            throw new MotorCurrentlyRunningException(currentState.get());
        }

        //start StepMotor actual stepping
        try {
            //execute each step one by one, at the same time checking interruption flag
            for (int i = 0; i < Math.abs(stepCount); i++) {

                //check if execution get interrupted
                if (stopRequested.get()) {
                    throw new InterruptedException();
                }

                //step only one step
                motorComponent.doStep(direction == StepDirection.CLOCK_WISE);
                ((StepperMotorRunningState) (currentState.get())).incrementExecutedStepCount();
            }
        } finally {
            motorComponent.stop();
            this.currentState.set(StepperMotorStopState.stopped());
            lock.lock();
            try {
                stoppedRunning.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Stops <b>only</b> currently moving stepper motor.
     * Waits until <b>currently</b> request step command is finished.
     */
    @Override
    public void stop() {
        //we check stopRequested additionally, because it's possible that some other thread
        //requested to move after first stop request.
        lock.lock();
        try {
            if (getCurrentState().isRunning()) {
                stopRequested.set(true);
                try {
                    stoppedRunning.await();
                } catch (InterruptedException e) {
                    //stop considered to be atomic operation, so not interruption is expected
                }
            }
        } finally {
            stopRequested.set(false);
            lock.unlock();
        }
    }

    @Override
    public StepperMotorState getCurrentState() {
        return currentState.get();
    }

    public AdaptedStepperMotorComponent getMotorComponent() {
        return motorComponent;
    }
}
