package org.pi4jrest.pins.i2c.exceptions;

public class I2cDeviceNotFound extends RuntimeException {

    private int address;

    public I2cDeviceNotFound(int address) {
        super("Can't find i2c device in following address: " + address);
        this.address = address;
    }

    public int getAddress() {
        return address;
    }
}
