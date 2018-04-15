package org.pi4jrest.pins.web;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.pi4jrest.pins.model.RaspberryPiGpioPinDescription;
import org.pi4jrest.pins.services.facade.GpioPinServiceFacade;
import org.pi4jrest.pins.services.simple.RaspberryPiGpioPinBoardManager;
import org.pi4jrest.pins.web.mappers.Pin2PinViewMapper;
import org.pi4jrest.pins.web.requests.PinStateRequest;
import org.pi4jrest.pins.web.responses.PinView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * Works with default raspberry pi board.
 */
@RestController
@RequestMapping({"/boards/raspberrypi/gpio/pins", "/gpio/pins"})
public class GpioRestController {

    private final RaspberryPiGpioPinBoardManager gpioPinBoardManager;
    private final GpioPinServiceFacade gpioPinServiceFacade;

    public GpioRestController(RaspberryPiGpioPinBoardManager gpioPinBoardManager, GpioPinServiceFacade gpioPinServiceFacade) {
        this.gpioPinBoardManager = gpioPinBoardManager;
        this.gpioPinServiceFacade = gpioPinServiceFacade;
    }

    @GetMapping(value = "/")
    public List<PinView> getOutputPins() {
        Collection<GpioPinDigitalOutput> provisionedPins = gpioPinBoardManager.getOutputPins();
        return Pin2PinViewMapper.map(provisionedPins);
    }

    @DeleteMapping(value = "/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableAllPins() {
        gpioPinBoardManager.disableOutputPins();
    }

    @GetMapping(value = "/{pinNumber}")
    public PinView getPin(@PathVariable int pinNumber) {
        GpioPinDigitalOutput gpioPinDigitalOutput = gpioPinServiceFacade.setupOutputPin(new RaspberryPiGpioPinDescription(pinNumber));
        return Pin2PinViewMapper.map(gpioPinDigitalOutput);
    }

    @PostMapping(value = "/{pinNumber}", params = "toggle")
    public PinView toggle(@PathVariable int pinNumber) {
        GpioPinDigitalOutput pinDigitalOutput = gpioPinServiceFacade.toggle(new RaspberryPiGpioPinDescription(pinNumber));
        return Pin2PinViewMapper.map(pinDigitalOutput);
    }

    @PutMapping(value = "/{pinNumber}")
    public PinView updatePinState(@PathVariable int pinNumber,
                                  @RequestBody @Valid PinStateRequest pinStateRequest) {
        GpioPinDigitalOutput pinDigitalOutput = gpioPinServiceFacade.setupOutputPin(new RaspberryPiGpioPinDescription(pinNumber));
        pinDigitalOutput.setState(pinStateRequest.getState());
        return Pin2PinViewMapper.map(pinDigitalOutput);
    }
}
