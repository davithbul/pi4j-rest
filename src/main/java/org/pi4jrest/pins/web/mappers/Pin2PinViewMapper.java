package org.pi4jrest.pins.web.mappers;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.pi4jrest.pins.web.responses.PinView;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Pin2PinViewMapper {

    public static PinView map(GpioPinDigitalOutput pinDigitalOutput) {
        return new PinView(pinDigitalOutput.getPin().getAddress(), pinDigitalOutput.getState());
    }

    public static List<PinView> map(Collection<GpioPinDigitalOutput> pinDigitalOutputCollection) {
        return pinDigitalOutputCollection.stream()
                .map(Pin2PinViewMapper::map)
                .collect(Collectors.toList());
    }
}
