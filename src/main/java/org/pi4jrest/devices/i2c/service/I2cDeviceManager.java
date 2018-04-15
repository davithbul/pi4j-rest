package org.pi4jrest.devices.i2c.service;

import com.pi4j.io.i2c.I2CFactory;
import org.pi4jrest.common.exceptions.ObjectAlreadyExistsException;
import org.pi4jrest.common.exceptions.ObjectNotFoundException;
import org.pi4jrest.pins.i2c.factory.I2cGpioPinManagerFactory;
import org.pi4jrest.pins.i2c.services.I2CGpioPinBoardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
public class I2cDeviceManager {

    private final static Logger log = LoggerFactory.getLogger(I2cDeviceManager.class);

    private final I2cGpioPinManagerFactory i2cGpioPinManagerFactory;

    public I2cDeviceManager(I2cGpioPinManagerFactory i2cGpioPinManagerFactory) {
        this.i2cGpioPinManagerFactory = i2cGpioPinManagerFactory;
    }

    public void registerI2cDevice(int busNumber) throws IOException, I2CFactory.UnsupportedBusNumberException {
        if (i2cGpioPinManagerFactory.isI2cBoardExists(busNumber)) {
            throw new ObjectAlreadyExistsException("I2C board with busNumber " + busNumber + " already exists.");
        }

        log.info("Registering i2c bus on number {}", busNumber);
        i2cGpioPinManagerFactory.registerI2cBoard(busNumber);
    }

    public void removeI2cDevice(int busNumber) {
        I2CGpioPinBoardManager i2cBoardPinManager = i2cGpioPinManagerFactory.getI2cBoardPinManager(busNumber);
        if (i2cBoardPinManager == null) {
            throw new ObjectNotFoundException("I2C board with busNumber " + busNumber + " doesn't exist.");
        }
        i2cGpioPinManagerFactory.remove(i2cBoardPinManager);
    }

    public Collection<I2CGpioPinBoardManager> list() {
        return i2cGpioPinManagerFactory.getCurrentlyRegisteredI2cBoards();
    }
}
