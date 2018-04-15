package org.pi4jrest.motors.stepper.model.pi4j;

import com.pi4j.component.motor.MotorBase;
import org.pi4jrest.motors.exceptions.MotorCurrentlyRunningException;
import org.pi4jrest.motors.stepper.exceptions.InvalidStepException;
import org.pi4jrest.motors.stepper.model.StepDirection;
import org.pi4jrest.motors.stepper.model.StepType;
import org.pi4jrest.motors.stepper.model.StepperMotor;
import org.pi4jrest.motors.stepper.model.StepperMotorState;
import org.pi4jrest.motors.stepper.model.adapters.AdaptedStepperMotorComponent;
import org.pi4jrest.motors.stepper.model.states.StepperMotorRunningState;
import org.pi4jrest.motors.stepper.model.states.StepperMotorStopState;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Stepper motor implementation, which controls group of stepper
 * motors movement. Guarantees synchronized movement of all stepper motors
 * belonging to the same group.
 */
public class StepperMotorGroup implements StepperMotor {

    /**
     * Represents current state of stepper motor.
     */
    private final AtomicReference<StepperMotorState> currentState = new AtomicReference<>(StepperMotorStopState.stopped());

    private final Collection<AdaptedStepperMotorComponent> gpioStepperMotorComponents;
    private final Lock lock = new ReentrantLock();
    private final Condition stoppedRunning = lock.newCondition();
    /**
     * Flag which shows if stop get requested.
     */
    private AtomicBoolean stopRequested = new AtomicBoolean(false);

    public StepperMotorGroup(Collection<AdaptedStepperMotorComponent> gpioStepperMotorComponents) {
        this.gpioStepperMotorComponents = gpioStepperMotorComponents;
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

        gpioStepperMotorComponents.forEach(gpioStepperMotorComponent -> gpioStepperMotorComponent.setStepInterval(NANOSECONDS.toMillis(stepInterval), stepInterval % 1000000));

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
                for (AdaptedStepperMotorComponent gpioStepperMotorComponent : gpioStepperMotorComponents) {
                    gpioStepperMotorComponent.doStep(direction == StepDirection.CLOCK_WISE);
                }

                ((StepperMotorRunningState) (currentState.get())).incrementExecutedStepCount();
            }
        } finally {
            gpioStepperMotorComponents.forEach(MotorBase::stop);
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
     * Stops <b>a<ll/b> currently moving stepper motors.
     * Waits until <b>currently</b> requested step command is finished.
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
}
