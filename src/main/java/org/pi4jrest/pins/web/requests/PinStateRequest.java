package org.pi4jrest.pins.web.requests;

import com.pi4j.io.gpio.PinState;

import javax.validation.constraints.NotNull;

public class PinStateRequest {

    @NotNull
    private PinState state;

    public PinState getState() {
        return state;
    }

    public void setState(PinState state) {
        this.state = state;
    }
}
