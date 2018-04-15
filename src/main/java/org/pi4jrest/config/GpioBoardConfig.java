package org.pi4jrest.config;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioProvider;
import org.pi4jrest.pins.services.simple.RaspberryPiGpioPinBoardManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Used to create different configuration for different boards -
 * raspberry pi board, i2c board1, i2c board2, etc...
 */
@Configuration
public class GpioBoardConfig {

    /**
     * This is default provisioning service, which provisions pi board.
     */
    @Bean
    public RaspberryPiGpioPinBoardManager raspberryPiGpioPinBoardManager(@Qualifier("raspberryPiGpioController") GpioController gpioController, @Qualifier("raspberryPiGpioProvider") GpioProvider gpioProvider) {
        return new RaspberryPiGpioPinBoardManager(gpioController, gpioProvider);
    }
}
