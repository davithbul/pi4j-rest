package org.pi4jrest.pins.web.responses;

import com.pi4j.io.gpio.PinState;

public class I2cPinView {
    private int busNumber;
    private String pinName;
    private PinState pinState;

    public I2cPinView(int busNumber, String pinName, PinState pinState) {
        this.busNumber = busNumber;
        this.pinName = pinName;
        this.pinState = pinState;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public String getPinName() {
        return pinName;
    }

    public PinState getPinState() {
        return pinState;
    }
}
