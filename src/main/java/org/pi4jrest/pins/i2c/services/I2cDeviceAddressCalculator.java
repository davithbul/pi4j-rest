package org.pi4jrest.pins.i2c.services;

import com.google.common.base.Preconditions;

public class I2cDeviceAddressCalculator {

    private final static int FIRST_DEVICE_ADDRESS = 0X20;

    public static int getAddress(int busNumber) {
        Preconditions.checkArgument(busNumber > 0, "Bus number should be greater than 0");
        Preconditions.checkArgument(busNumber <= 8, "Bus number should be less than 0");
        return getFirstDeviceAddress() + (busNumber - 1);
    }

    public static int getNumber(int bussAddress) {
        Preconditions.checkArgument(bussAddress >= FIRST_DEVICE_ADDRESS, "Bus number should be greater than or equal to " + FIRST_DEVICE_ADDRESS);
        Preconditions.checkArgument(bussAddress <= FIRST_DEVICE_ADDRESS + 8, "Bus number should be less than or equal to " + FIRST_DEVICE_ADDRESS + 8);
        return bussAddress - getFirstDeviceAddress() + 1;
    }

    public static int getFirstDeviceAddress() {
        return FIRST_DEVICE_ADDRESS;
    }
}
