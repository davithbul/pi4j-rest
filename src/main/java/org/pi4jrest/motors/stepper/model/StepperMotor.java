package org.pi4jrest.motors.stepper.model;


import org.pi4jrest.motors.model.Motor;

/**
 * {@link StepperMotor} represents model of Stepper motor with
 * all the basic functionality which stepper motor can provide.
 * It has all the information about current state of stepper
 * motor, and possibility to control, monitor it.
 */
public interface StepperMotor extends Motor {

    /**
     * Steps given stepCount. If step count is positive then it moves clock wise,
     * otherwise counter to clock wise.
     *
     * @param stepCount number of steps which stepper motor should execute.
     * @throws InterruptedException if interruption was issued during execution.
     */
    void step(int stepCount) throws InterruptedException;

    /**
     * Steps given step count
     *
     * @param stepCount    number of steps which stepper motor should move
     * @param stepType     the type of move how each step will be executed
     * @param stepInterval pause in nanoseconds between executed steps
     * @throws InterruptedException if interruption was issued during execution
     */
    void step(int stepCount, StepType stepType, int stepInterval) throws InterruptedException;

    /**
     * Stops stepper motor if it's running currently.
     */
    void stop();

    /**
     * Returns current state of the stepper motor.
     */
    StepperMotorState getCurrentState();
}
