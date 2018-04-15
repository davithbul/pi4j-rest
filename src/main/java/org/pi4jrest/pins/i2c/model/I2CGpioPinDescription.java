package org.pi4jrest.pins.i2c.model;

import org.pi4jrest.pins.model.BoardType;
import org.pi4jrest.pins.model.GpioPinDescription;

public class I2CGpioPinDescription implements GpioPinDescription {
    private final int pinAddress;
    private final int busNumber;

    public I2CGpioPinDescription(int pinAddress, int busNumber) {
        this.pinAddress = pinAddress;
        this.busNumber = busNumber;
    }

    @Override
    public BoardType getBoardType() {
        return BoardType.I2C;
    }

    @Override
    public int getPinAddress() {
        return pinAddress;
    }

    public int getBusNumber() {
        return busNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof I2CGpioPinDescription)) return false;

        I2CGpioPinDescription that = (I2CGpioPinDescription) o;

        if (getPinAddress() != that.getPinAddress()) return false;
        if (getBusNumber() != that.getBusNumber()) return false;
        return getBoardType() == that.getBoardType();
    }

    @Override
    public int hashCode() {
        int result = getPinAddress();
        result = 31 * result + getBusNumber();
        result = 31 * result + getBoardType().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "I2CGpioPinDescription{" +
                "pinAddress=" + getPinAddress() +
                ", busNumber=" + getBusNumber() +
                ", boardType=" + getBoardType() +
                '}';
    }
}
