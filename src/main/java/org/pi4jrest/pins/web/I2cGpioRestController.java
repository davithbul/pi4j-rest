package org.pi4jrest.pins.web;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.pi4jrest.pins.i2c.model.I2CGpioPinDescription;
import org.pi4jrest.pins.services.facade.GpioPinServiceFacade;
import org.pi4jrest.pins.web.mappers.I2cPin2PinViewMapper;
import org.pi4jrest.pins.web.mappers.Pin2PinViewMapper;
import org.pi4jrest.pins.web.requests.PinStateRequest;
import org.pi4jrest.pins.web.responses.I2cPinView;
import org.pi4jrest.pins.web.responses.PinView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/boards/i2c/buses/{busNumber}/gpio/pins")
public class I2cGpioRestController {

    private GpioPinServiceFacade gpioPinServiceFacade;

    public I2cGpioRestController(GpioPinServiceFacade gpioPinServiceFacade) {
        this.gpioPinServiceFacade = gpioPinServiceFacade;
    }

    @GetMapping(value = "/")
    public List<PinView> getI2cOutputPins(@PathVariable int busNumber) {
        Collection<GpioPinDigitalOutput> provisionedPins = gpioPinServiceFacade.getI2cDeviceOutputPins(busNumber);
        return Pin2PinViewMapper.map(provisionedPins);
    }

    @DeleteMapping(value = "/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableAllPins(@PathVariable int busNumber) {
        gpioPinServiceFacade.disableI2cDeviceOutputPins(busNumber);
    }

    @GetMapping(value = "/{pinAddress}")
    public I2cPinView getPin(@PathVariable int busNumber, @PathVariable int pinAddress) {
        GpioPinDigitalOutput gpioPinDigitalOutput = gpioPinServiceFacade.setupOutputPin(
                new I2CGpioPinDescription(pinAddress, busNumber));
        return I2cPin2PinViewMapper.map(busNumber, gpioPinDigitalOutput);
    }

    @PostMapping(value = "/{pinAddress}", params = "toggle")
    public I2cPinView toggle(@PathVariable int busNumber, @PathVariable int pinAddress) {
        GpioPinDigitalOutput pinDigitalOutput = gpioPinServiceFacade.toggle(
                new I2CGpioPinDescription(pinAddress, busNumber));
        return I2cPin2PinViewMapper.map(busNumber, pinDigitalOutput);
    }

    @PutMapping(value = "/{pinAddress}")
    public I2cPinView updatePinState(@PathVariable int busNumber, @PathVariable int pinAddress,
                                     @RequestBody @Valid PinStateRequest pinStateRequest) {

        GpioPinDigitalOutput pinDigitalOutput = gpioPinServiceFacade.setupOutputPin(
                new I2CGpioPinDescription(pinAddress, busNumber));
        pinDigitalOutput.setState(pinStateRequest.getState());
        return I2cPin2PinViewMapper.map(busNumber, pinDigitalOutput);
    }
}
