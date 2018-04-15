package org.pi4jrest.pins.exceptions;

public class PinNotFoundException extends RuntimeException {

    private int pinNumber;

    public PinNotFoundException(int pinNumber) {
        super("Pin not found having number: " + pinNumber);
        this.pinNumber = pinNumber;
    }

    public PinNotFoundException(String message, int pinNumber) {
        super(message);
        this.pinNumber = pinNumber;
    }

    public int getPinNumber() {
        return pinNumber;
    }
}
