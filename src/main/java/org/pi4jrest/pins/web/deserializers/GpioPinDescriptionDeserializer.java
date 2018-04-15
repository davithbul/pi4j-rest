package org.pi4jrest.pins.web.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.pi4jrest.pins.exceptions.BoardPinProviderNotFoundException;
import org.pi4jrest.pins.i2c.model.I2CGpioPinDescription;
import org.pi4jrest.pins.model.BoardType;
import org.pi4jrest.pins.model.GpioPinDescription;
import org.pi4jrest.pins.model.RaspberryPiGpioPinDescription;

import java.io.IOException;

public class GpioPinDescriptionDeserializer extends StdDeserializer<GpioPinDescription> {

    public GpioPinDescriptionDeserializer() {
        this(null);
    }

    public GpioPinDescriptionDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public GpioPinDescription deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        BoardType boardType = node.has("boardType") ? BoardType.valueOf(node.get("boardType").asText()) : BoardType.RASPBERRY_PI;

        if (boardType == BoardType.RASPBERRY_PI) {
            return new RaspberryPiGpioPinDescription(node.get("pinAddress").asInt());
        } else if (boardType == BoardType.I2C) {
            return new I2CGpioPinDescription(
                    node.get("pinAddress").asInt(),
                    node.get("busNumber").asInt());
        } else {
            throw new BoardPinProviderNotFoundException(boardType);
        }
    }
}
