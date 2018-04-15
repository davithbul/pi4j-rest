package org.pi4jrest.config;

import com.pi4j.io.gpio.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("!dev")
@Configuration
public class GpioConfig implements DisposableBean {

    static {
        GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
    }

    @Bean
    public GpioController raspberryPiGpioController() {
        GpioController gpioController = GpioFactory.getInstance();
        gpioController.setShutdownOptions(true, PinState.LOW);
        return gpioController;
    }

    @Bean
    public GpioProvider raspberryPiGpioProvider() {
        return GpioFactory.getDefaultProvider();
    }

//    @Bean
//    public LocalServoBlasterProvider localServoBlasterProvider() throws IOException {
//        return new LocalServoBlasterProvider();
//    }

    @Override
    public void destroy() throws Exception {
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        raspberryPiGpioController().shutdown();
    }
}
