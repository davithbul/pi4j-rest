package org.pi4jrest.motors.stepper.model.pi4j;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.pi4jrest.motors.exceptions.MotorCurrentlyRunningException;
import org.pi4jrest.motors.stepper.exceptions.InvalidStepException;
import org.pi4jrest.motors.stepper.model.StepperMotorState;
import org.pi4jrest.motors.stepper.model.adapters.AdaptedStepperMotorComponent;
import org.pi4jrest.motors.stepper.model.states.StepperMotorStopState;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.util.Assert.isTrue;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStepperMotorTest {

    private DefaultStepperMotor stepperMotor;

    @Mock
    private AdaptedStepperMotorComponent stepperMotorComponent;

    @Before
    public void setUp() throws Exception {
        GpioPinDigitalOutput[] gpioPinDigitalOutputs = IntStream.range(0, 4)
                .mapToObj(i -> Mockito.mock(GpioPinDigitalOutput.class))
                .toArray(GpioPinDigitalOutput[]::new);
        stepperMotor = new DefaultStepperMotor(gpioPinDigitalOutputs);
        ReflectionTestUtils.setField(stepperMotor, "motorComponent", stepperMotorComponent);
    }

    @Test
    public void testClockWiseStepping() throws InterruptedException {
        stepperMotor.step(10);
        verify(stepperMotorComponent, times(10)).doStep(true);
    }

    @Test
    public void testCounterClockWiseStepping() throws InterruptedException {
        stepperMotor.step(-10);
        verify(stepperMotorComponent, times(10)).doStep(false);
    }

    @Test
    public void test0StepRequest() throws InterruptedException {
        boolean failed = false;
        try {
            stepperMotor.step(0);
        } catch (InvalidStepException e) {
            failed = true;
        }

        isTrue(failed, "0 stepping should not be allowed.");
        verify(stepperMotorComponent, times(0)).doStep(true);
        verify(stepperMotorComponent, times(0)).doStep(false);
    }

    @Test
    public void testRequestNewRunWhileThereIsRunningStep() throws InterruptedException, ExecutionException {
        Mockito.doAnswer(invocation -> {
            //sleep after 5th step
            TimeUnit.MILLISECONDS.sleep(100);
            return null;
        }).when(stepperMotorComponent).doStep(true);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<?> clockWiseStepping = executorService.submit(() -> {
            try {
                stepperMotor.step(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        TimeUnit.SECONDS.sleep(1);

        Future<?> oppositeStepping = executorService.submit(() -> {
            try {
                stepperMotor.step(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        clockWiseStepping.get();


        boolean failed = false;
        try {
            oppositeStepping.get();
            stepperMotor.step(20);
        } catch (ExecutionException e) {
            failed = true;
            Assert.isAssignable(MotorCurrentlyRunningException.class, e.getCause().getClass());
        }

        verify(stepperMotorComponent, times(10)).doStep(true);
        isTrue(failed, "Second stepper motor move call should be failed");
    }

    @Test
    public void testInterruptionDuringStepping() throws InterruptedException {
        AtomicInteger executedStepCount = new AtomicInteger(0);

        Mockito.doAnswer(invocation -> {
            //sleep after 5th step
            if (executedStepCount.incrementAndGet() == 5) {
                TimeUnit.SECONDS.sleep(5);
            }
            return null;
        }).when(stepperMotorComponent).doStep(true);

        AtomicBoolean interruptionHappened = new AtomicBoolean(false);
        Thread stepExecutingThread = new Thread(() -> {
            try {
                stepperMotor.step(100);
            } catch (InterruptedException e) {
                interruptionHappened.set(true);
            }
        });
        stepExecutingThread.start();

        TimeUnit.SECONDS.sleep(2);
        stepperMotor.stop();

        //wait until step execution finishes
        stepExecutingThread.join();

        StepperMotorState currentState = stepperMotor.getCurrentState();

        Assert.isAssignable(StepperMotorStopState.class, currentState.getClass());
        isTrue(interruptionHappened.get(), "StepperMotor should be interrupted");
        isTrue(!currentState.isRunning(), "Stepper motor should be stopped");

        verify(stepperMotorComponent, times(5)).doStep(true);
    }
}
