package org.pi4jrest.pins.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.pi4jrest.pins.web.deserializers.GpioPinDescriptionDeserializer;

import java.io.Serializable;

/**
 * Represents description of {@link com.pi4j.io.gpio.GpioPin}.
 * Unlike {@link com.pi4j.io.gpio.GpioPin} object this is only description
 * and has no live connection with board pins.
 * It can be used to represent both Input and output pins.
 */
@JsonDeserialize(using = GpioPinDescriptionDeserializer.class)
public interface GpioPinDescription extends Serializable {

    /**
     * Returns numeric address of the pin in the board.
     */
    int getPinAddress();

    /**
     * Returns the board type for which the pin is defined.
     */
    BoardType getBoardType();


}
