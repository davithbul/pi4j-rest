package org.pi4jrest.motors.stepper.model;

import org.pi4jrest.pins.model.GpioPinDescription;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents stepper motor connection to real board/bus/etc.
 */
@Immutable
public class StepperMotorConnection implements Serializable {
    private final String name;
    private final GpioPinDescription pin1;
    private final GpioPinDescription pin2;
    private final GpioPinDescription pin3;
    private final GpioPinDescription pin4;

    public StepperMotorConnection(String name, GpioPinDescription pin1, GpioPinDescription pin2, GpioPinDescription pin3, GpioPinDescription pin4) {
        this.name = Objects.requireNonNull(name);
        this.pin1 = Objects.requireNonNull(pin1);
        this.pin2 = Objects.requireNonNull(pin2);
        this.pin3 = Objects.requireNonNull(pin3);
        this.pin4 = Objects.requireNonNull(pin4);

        //validate that pins are unique
        long distinctPinCount = Stream.of(pin1, pin2, pin3, pin4)
                .filter(Objects::nonNull)
                .distinct().count();
        if (distinctPinCount != 4) {
            throw new IllegalArgumentException("Pin values should be unique.");
        }
    }

    public String getName() {
        return name;
    }

    public GpioPinDescription getPin1() {
        return pin1;
    }

    public GpioPinDescription getPin2() {
        return pin2;
    }

    public GpioPinDescription getPin3() {
        return pin3;
    }

    public GpioPinDescription getPin4() {
        return pin4;
    }
}
