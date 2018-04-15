package org.pi4jrest.pins.services.facade;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import org.pi4jrest.pins.exceptions.BoardPinProviderNotFoundException;
import org.pi4jrest.pins.i2c.factory.I2cGpioPinManagerFactory;
import org.pi4jrest.pins.i2c.model.I2CGpioPinDescription;
import org.pi4jrest.pins.model.BoardType;
import org.pi4jrest.pins.model.GpioPinDescription;
import org.pi4jrest.pins.services.GpioPinBoardManager;
import org.pi4jrest.pins.services.simple.RaspberryPiGpioPinBoardManager;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Provides generic interface to work with multiple gpio pins' boards.
 */
@Service
public class GpioPinServiceFacade {

    private I2cGpioPinManagerFactory i2cGpioPinManagerFactory;
    private RaspberryPiGpioPinBoardManager raspberryPiGpioPinBoardManager;
    private PinDescriptionMapper pinDescriptionMapper;

    public GpioPinServiceFacade(I2cGpioPinManagerFactory i2cGpioPinManagerFactory, RaspberryPiGpioPinBoardManager raspberryPiGpioPinBoardManager, PinDescriptionMapper pinDescriptionMapper) {
        this.i2cGpioPinManagerFactory = i2cGpioPinManagerFactory;
        this.raspberryPiGpioPinBoardManager = raspberryPiGpioPinBoardManager;
        this.pinDescriptionMapper = pinDescriptionMapper;
    }

    public GpioPinDigitalOutput toggle(GpioPinDescription gpioPinDescription) {
        Pin pin = pinDescriptionMapper.getPin(gpioPinDescription);
        return getBoardPinManager(gpioPinDescription).toggle(pin);
    }

    public GpioPinDigitalOutput setupOutputPin(GpioPinDescription pinDescription) {
        Pin pin = pinDescriptionMapper.getPin(pinDescription);
        return getBoardPinManager(pinDescription).setupOutputPin(pin);
    }

    public Collection<GpioPinDigitalOutput> getI2cDeviceOutputPins(int busNumber) {
        GpioPinBoardManager pinService = i2cGpioPinManagerFactory.getI2cBoardPinManager(busNumber);
        return pinService.getOutputPins();
    }

    public void disableI2cDeviceOutputPins(int busNumber) {
        GpioPinBoardManager pinService = i2cGpioPinManagerFactory.getI2cBoardPinManager(busNumber);
        pinService.disableOutputPins();
    }

    private GpioPinBoardManager getBoardPinManager(GpioPinDescription gpioPinDescription) {
        if (gpioPinDescription.getBoardType() == BoardType.RASPBERRY_PI) {
            return raspberryPiGpioPinBoardManager;
        } else if (gpioPinDescription.getBoardType() == BoardType.I2C) {
            return i2cGpioPinManagerFactory.getI2cBoardPinManager(((I2CGpioPinDescription) gpioPinDescription).getBusNumber());
        } else {
            throw new BoardPinProviderNotFoundException(gpioPinDescription.getBoardType());
        }
    }
}
