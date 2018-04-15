package org.pi4jrest.config.dev.mocks;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.impl.I2CDeviceImpl;

import java.util.HashMap;
import java.util.Map;

import static com.pi4j.io.gpio.PinMode.DIGITAL_OUTPUT;

public class I2cGpioDevProvider implements GpioProvider {

    private static final String NAME = "com.pi4j.gpio.extension.mcp.MCP23017GpioProvider";

    private Map<Pin, PinState> pinStateMap = new HashMap<>();

    private I2CDevice device;

    public I2cGpioDevProvider(int busAddress) {
        device = new I2CDeviceImpl(null, busAddress);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean hasPin(Pin pin) {
        return false;
    }

    @Override
    public void export(Pin pin, PinMode mode, PinState defaultState) {

    }

    @Override
    public void export(Pin pin, PinMode mode) {

    }

    @Override
    public boolean isExported(Pin pin) {
        return false;
    }

    @Override
    public void unexport(Pin pin) {

    }

    @Override
    public void setMode(Pin pin, PinMode mode) {

    }

    @Override
    public PinMode getMode(Pin pin) {
        return DIGITAL_OUTPUT;
    }

    @Override
    public void setPullResistance(Pin pin, PinPullResistance resistance) {

    }

    @Override
    public PinPullResistance getPullResistance(Pin pin) {
        return PinPullResistance.PULL_UP;
    }

    @Override
    public void setState(Pin pin, PinState state) {
        pinStateMap.put(pin, state);
    }

    @Override
    public PinState getState(Pin pin) {
        return pinStateMap.getOrDefault(pin, PinState.LOW);
    }

    @Override
    public void setValue(Pin pin, double value) {

    }

    @Override
    public double getValue(Pin pin) {
        return 0;
    }

    @Override
    public void setPwm(Pin pin, int value) {

    }

    @Override
    public void setPwmRange(Pin pin, int range) {

    }

    @Override
    public int getPwm(Pin pin) {
        return 0;
    }

    @Override
    public void addListener(Pin pin, PinListener listener) {

    }

    @Override
    public void removeListener(Pin pin, PinListener listener) {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isShutdown() {
        return false;
    }
}
