package org.pi4jrest.config.dev.i2c.service;

import com.pi4j.io.i2c.I2CFactory;
import org.pi4jrest.config.dev.mocks.I2cGpioDevProvider;
import org.pi4jrest.pins.i2c.factory.I2cGpioPinManagerFactory;
import org.pi4jrest.pins.i2c.services.I2CGpioPinBoardManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Profile("dev")
@Service
public class DevI2cGpioPinManagerFactory extends I2cGpioPinManagerFactory {

    @Override
    protected I2CGpioPinBoardManager initBoardManager(int busAddress) throws IOException, I2CFactory.UnsupportedBusNumberException {
        I2cGpioDevProvider i2cGpioDevProvider = new I2cGpioDevProvider(busAddress);
        return new I2CGpioPinBoardManager(i2cGpioDevProvider);
    }
}
