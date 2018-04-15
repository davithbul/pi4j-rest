package org.pi4jrest.pins.i2c.factory;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import org.pi4jrest.common.web.exception.handler.GlobalExceptionHandler;
import org.pi4jrest.pins.i2c.exceptions.I2cDeviceNotFound;
import org.pi4jrest.pins.i2c.services.I2CGpioPinBoardManager;
import org.pi4jrest.pins.i2c.services.I2cDeviceAddressCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * It's a factory for i2CGpioPinBoardManager beans. It uses the same bean lifecycle
 * management as spring does.
 */
@Profile("!dev")
@Component
public class I2cGpioPinManagerFactory {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final Collection<I2CGpioPinBoardManager> i2CGpioPinBoardManagers;

    public I2cGpioPinManagerFactory() {
        this.i2CGpioPinBoardManagers = new LinkedList<>();
    }

    public void registerI2cBoard(int busNumber) throws IOException, I2CFactory.UnsupportedBusNumberException {
        int busAddress = I2cDeviceAddressCalculator.getAddress(busNumber);
        I2CGpioPinBoardManager i2CGpioPinBoardManager = initBoardManager(busAddress);
        i2CGpioPinBoardManagers.add(i2CGpioPinBoardManager);
    }

    public void remove(I2CGpioPinBoardManager i2CGpioPinBoardManager) {
        i2CGpioPinBoardManagers.remove(i2CGpioPinBoardManager);
        i2CGpioPinBoardManager.destroy();
    }

    public I2CGpioPinBoardManager getI2cBoardPinManager(int busNumber) {
        int busAddress = I2cDeviceAddressCalculator.getAddress(busNumber);
        return i2CGpioPinBoardManagers.stream()
                .filter(i2cGpioPinService -> i2cGpioPinService.getI2cDevice().getAddress() == busAddress)
                .findAny().orElseThrow(() -> new I2cDeviceNotFound(busAddress));
    }

    public boolean isI2cBoardExists(int busNumber) {
        int busAddress = I2cDeviceAddressCalculator.getAddress(busNumber);
        return i2CGpioPinBoardManagers.stream()
                .anyMatch(i2cGpioPinService -> i2cGpioPinService.getI2cDevice().getAddress() == busAddress);
    }

    public Collection<I2CGpioPinBoardManager> getCurrentlyRegisteredI2cBoards() {
        return i2CGpioPinBoardManagers;
    }

    protected I2CGpioPinBoardManager initBoardManager(int busAddress) throws IOException, I2CFactory.UnsupportedBusNumberException {
        //we use BUS_1 because it's the same i2cdetect -y 1, it's the same one
        MCP23017GpioProvider provider = new MCP23017GpioProvider(I2CBus.BUS_1, busAddress);
        return new I2CGpioPinBoardManager(provider);
    }

    @PostConstruct
    public void initAllI2cBuses() {
        for (int busNumber = 1; busNumber <= 8; busNumber++) {
            try {
                I2CGpioPinBoardManager i2CGpioPinBoardManager = initBoardManager(I2cDeviceAddressCalculator.getAddress(busNumber));
                i2CGpioPinBoardManagers.add(i2CGpioPinBoardManager);
                log.info("initializing i2c bus with address {}", I2cDeviceAddressCalculator.getAddress(busNumber));
            } catch (IOException | I2CFactory.UnsupportedBusNumberException e) {
                log.info("Can't initialize i2c bus with address {}", I2cDeviceAddressCalculator.getAddress(busNumber));
            }
        }
    }
}
