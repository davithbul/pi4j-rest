package org.pi4jrest.pins.i2c;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.io.IOException;


public class MCP23017GpioExample {

    private final GpioController gpio;

    public MCP23017GpioExample(GpioController gpio) {
        this.gpio = gpio;
    }

    public void runExample() throws InterruptedException, UnsupportedBusNumberException, IOException {
        // create custom MCP23017 GPIO provider
        MCP23017GpioProvider provider = new MCP23017GpioProvider(I2CBus.BUS_1, 0x21);

        // provision gpio input pins from MCP23017
        GpioPinDigitalInput myInputs[] = {
                gpio.provisionDigitalInputPin(provider, MCP23017Pin.GPIO_A0, "MyInput-A0", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(provider, MCP23017Pin.GPIO_A1, "MyInput-A1", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(provider, MCP23017Pin.GPIO_A2, "MyInput-A2", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(provider, MCP23017Pin.GPIO_A3, "MyInput-A3", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(provider, MCP23017Pin.GPIO_A4, "MyInput-A4", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(provider, MCP23017Pin.GPIO_A5, "MyInput-A5", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(provider, MCP23017Pin.GPIO_A6, "MyInput-A6", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(provider, MCP23017Pin.GPIO_A7, "MyInput-A7", PinPullResistance.PULL_UP),
        };

        // create and register gpio pin listener
        gpio.addListener((GpioPinListenerDigital) event -> {
            // display pin state on console
            System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                    + event.getState());
        }, myInputs);

        // provision gpio output pins and make sure they are all LOW at startup
        GpioPinDigitalOutput myOutputs[] = {
                gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B0, "MyOutput-B0", PinState.LOW),
                gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B1, "MyOutput-B1", PinState.LOW),
                gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B2, "MyOutput-B2", PinState.LOW),
                gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B3, "MyOutput-B3", PinState.LOW),
                gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B4, "MyOutput-B4", PinState.LOW),
                gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B5, "MyOutput-B5", PinState.LOW),
                gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B6, "MyOutput-B6", PinState.LOW),
                gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B7, "MyOutput-B7", PinState.LOW)
        };

        // keep program running for 20 seconds
        for (int count = 0; count < 10; count++) {
            gpio.setState(true, myOutputs);
            Thread.sleep(1000);
            gpio.setState(false, myOutputs);
            Thread.sleep(1000);
        }
    }
}
