package org.pi4jrest.pins.services.simple;


import com.pi4j.io.gpio.*;
import org.pi4jrest.pins.services.GpioPinBoardManager;

import java.util.Collection;
import java.util.stream.Collectors;

public class RaspberryPiGpioPinBoardManager implements GpioPinBoardManager {

    private final GpioController gpioController;
    private final GpioProvider gpioProvider;

    public RaspberryPiGpioPinBoardManager(GpioController gpioController, GpioProvider gpioProvider) {
        this.gpioController = gpioController;
        this.gpioProvider = gpioProvider;
    }

    @Override
    public GpioPinDigitalOutput toggle(Pin pin) {
        GpioPinDigitalOutput outputDigitalPin = setupOutputPin(pin);
        if (outputDigitalPin.getState() == PinState.LOW) {
            outputDigitalPin.high();
        } else {
            outputDigitalPin.low();
        }
        return outputDigitalPin;
    }

    @Override
    public GpioPinDigitalOutput setupOutputPin(Pin pin) {
        GpioPinDigitalOutput pinDigitalOutput = (GpioPinDigitalOutput) gpioController.getProvisionedPin(pin);
        if (pinDigitalOutput == null) {
            try {
                pinDigitalOutput = gpioController.provisionDigitalOutputPin(gpioProvider, pin);
            } finally {
                setupShutdownOption(pinDigitalOutput);
            }
        }
        return pinDigitalOutput;
    }

    @Override
    public Collection<GpioPinDigitalOutput> getOutputPins() {
        Collection<GpioPin> gpioPins = gpioController.getProvisionedPins();
        return gpioPins.stream()
                .filter(gpioPin -> gpioPin.getProvider() == gpioProvider)
                .map(gpioPin -> this.setupOutputPin(gpioPin.getPin()))
                .collect(Collectors.toList());
    }

    @Override
    public void disableOutputPins() {
        getOutputPins()
                .forEach(provisionedPin -> {
                    provisionedPin.low();
                    gpioController.unprovisionPin(provisionedPin);
                });
    }

    private void setupShutdownOption(GpioPinDigitalOutput... pins) {
        // this will ensure that the motor is stopped when the program terminates
        gpioController.setShutdownOptions(true, PinState.LOW, pins);
    }
}
