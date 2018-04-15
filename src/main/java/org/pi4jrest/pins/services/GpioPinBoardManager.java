package org.pi4jrest.pins.services;


import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;

import java.util.Collection;

/**
 * Works with gpio pins, allowing to setup input / output pins
 * and changing state of those pins.
 * {@link GpioPinBoardManager} works with single fixed pin board.
 */
public interface GpioPinBoardManager {

    /**
     * Sets up pin as an output pin, or returns pin if it is already set up.
     */
    GpioPinDigitalOutput setupOutputPin(Pin pin);

    /**
     * toggles pin state from high to low.
     */
    GpioPinDigitalOutput toggle(Pin pin);

    /**
     * Returns all output pins in the board.
     */
    Collection<GpioPinDigitalOutput> getOutputPins();

    /**
     * Disables (sets low) all output pins in the board.
     */
    void disableOutputPins();
}
