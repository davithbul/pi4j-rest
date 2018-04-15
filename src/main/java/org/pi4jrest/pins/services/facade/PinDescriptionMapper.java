package org.pi4jrest.pins.services.facade;

import com.pi4j.io.gpio.Pin;
import org.pi4jrest.pins.exceptions.BoardPinProviderNotFoundException;
import org.pi4jrest.pins.i2c.manager.I2cPinProvider;
import org.pi4jrest.pins.model.BoardType;
import org.pi4jrest.pins.model.GpioPinDescription;
import org.pi4jrest.pins.services.RaspberryPiBoardPinProvider;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class PinDescriptionMapper {

    private Map<BoardType, Function<GpioPinDescription, Pin>> pinDescription2PinMapper = new HashMap<>();

    public PinDescriptionMapper(RaspberryPiBoardPinProvider pinManager, I2cPinProvider i2CPinProvider) {
        pinDescription2PinMapper.put(BoardType.RASPBERRY_PI, gpioPinDescription -> pinManager.getPin(gpioPinDescription.getPinAddress()));
        pinDescription2PinMapper.put(BoardType.I2C, gpioPinDescription -> i2CPinProvider.getPin(gpioPinDescription.getPinAddress()));
    }

    public void validatePin(@Nonnull GpioPinDescription gpioPinDescription) {
        getPin(gpioPinDescription);
    }

    public Pin getPin(@Nonnull GpioPinDescription gpioPinDescription) {
        Function<GpioPinDescription, Pin> gpioPinDescriptionPinFunction = pinDescription2PinMapper.get(gpioPinDescription.getBoardType());
        if (gpioPinDescriptionPinFunction == null) {
            throw new BoardPinProviderNotFoundException(gpioPinDescription.getBoardType());
        }

        return gpioPinDescriptionPinFunction.apply(gpioPinDescription);
    }
}