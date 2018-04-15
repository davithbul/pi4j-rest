package org.pi4jrest.pins.services;


import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiBcmPin;
import org.pi4jrest.pins.exceptions.PinNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RaspberryPiBoardPinProvider {

    public Pin getPin(int pinNumber) {
        Pin pin = RaspiBcmPin.getPinByAddress(pinNumber);
        if (pin == null) {
            throw new PinNotFoundException(pinNumber);
        }
        return pin;
    }
}
