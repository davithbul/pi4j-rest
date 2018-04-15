package org.pi4jrest.pins.i2c.exceptions;

import org.pi4jrest.pins.exceptions.PinNotFoundException;

public class I2cPinNotFound extends PinNotFoundException {

    public I2cPinNotFound(int pinAddress) {
        super("Can't find pin: " + pinAddress + " in i2c board.", pinAddress);
    }
}
