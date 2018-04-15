package org.pi4jrest.pins.i2c.manager;


import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.Pin;
import org.pi4jrest.pins.i2c.exceptions.I2cPinNotFound;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class I2cPinProvider {

    private static Map<Integer, Pin> pinAddress2PinMap = new HashMap<>();

    static {
        pinAddress2PinMap = Arrays.stream(MCP23017Pin.ALL)
                .collect(Collectors.toMap(Pin::getAddress, pin -> pin));
    }

    /**
     * @param i2cPinAddress The address of the pin in i2c board e.g. 16
     */
    public Pin getPin(int i2cPinAddress) {
        Pin pin = pinAddress2PinMap.get(i2cPinAddress);
        if (pin == null) {
            throw new I2cPinNotFound(i2cPinAddress);
        }
        return pin;
    }
}
