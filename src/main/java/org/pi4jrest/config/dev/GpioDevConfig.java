package org.pi4jrest.config.dev;

import com.pi4j.component.servo.ServoProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.impl.GpioControllerImpl;
import org.pi4jrest.config.dev.mocks.DevServoProvider;
import org.pi4jrest.config.dev.mocks.GpioDevProvider;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;


@Profile("dev")
@Configuration
public class GpioDevConfig implements DisposableBean {

    @Bean
    public GpioController raspberryPiGpioController() {
        return new GpioControllerImpl(raspberryPiGpioProvider());
    }

    @Bean
    public GpioProvider raspberryPiGpioProvider() {
        return new GpioDevProvider();
    }

    @Bean
    public ServoProvider rpiServoBlasterProvider() throws IOException {
        return new DevServoProvider();
    }

    @Override
    public void destroy() throws Exception {
        raspberryPiGpioController().shutdown();
    }
}
