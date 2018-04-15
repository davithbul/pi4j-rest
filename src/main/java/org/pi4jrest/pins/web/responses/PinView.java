package org.pi4jrest.pins.web.responses;

import com.pi4j.io.gpio.PinState;

public class PinView {
    private int number;
    private PinState pinState;

    public PinView(int number, PinState pinState) {
        this.number = number;
        this.pinState = pinState;
    }

    public int getNumber() {
        return number;
    }

    public PinState getPinState() {
        return pinState;
    }
}
