package org.pi4jrest.pins.i2c.services;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.impl.GpioControllerImpl;
import com.pi4j.io.i2c.I2CDevice;
import org.pi4jrest.common.ReflectionUtils;
import org.pi4jrest.pins.services.GpioPinBoardManager;
import org.pi4jrest.pins.services.simple.RaspberryPiGpioPinBoardManager;
import org.springframework.beans.factory.DisposableBean;

import java.util.Collection;

/**
 * Provides pin management for single i2c board.
 */
public class I2CGpioPinBoardManager implements GpioPinBoardManager, DisposableBean {

    private GpioControllerImpl gpioController;
    private GpioPinBoardManager realInstance;
    private I2CDevice i2cDevice;

    public I2CGpioPinBoardManager(GpioProvider provider) {
        //we need different gpioController for each board. The problem is that gpio controller
        //is tightly coupled with GPIOPins which are not unique from board to board.
        gpioController = new GpioControllerImpl(provider);
        realInstance = new RaspberryPiGpioPinBoardManager(gpioController, provider);
        i2cDevice = (I2CDevice) ReflectionUtils.getField(provider, "device");
    }

    @Override
    public GpioPinDigitalOutput toggle(Pin pin) {
        return realInstance.toggle(pin);
    }

    @Override
    public GpioPinDigitalOutput setupOutputPin(Pin pin) {
        return realInstance.setupOutputPin(pin);
    }

    @Override
    public Collection<GpioPinDigitalOutput> getOutputPins() {
        return realInstance.getOutputPins();
    }

    @Override
    public void disableOutputPins() {
        realInstance.disableOutputPins();
    }

    public I2CDevice getI2cDevice() {
        return i2cDevice;
    }

    @Override
    public void destroy() {
        gpioController.shutdown();
    }
}
