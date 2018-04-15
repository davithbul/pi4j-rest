package org.pi4jrest.pins.web.mappers;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.pi4jrest.pins.web.responses.I2cPinView;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class I2cPin2PinViewMapper {

    public static I2cPinView map(int busNumber, GpioPinDigitalOutput pinDigitalOutput) {
        return new I2cPinView(busNumber, pinDigitalOutput.getName(), pinDigitalOutput.getState());
    }

    public static List<I2cPinView> map(int busNumber, Collection<GpioPinDigitalOutput> pinDigitalOutputCollection) {
        return pinDigitalOutputCollection.stream()
                .map(pin -> map(busNumber, pin))
                .collect(Collectors.toList());
    }
}
