package org.pi4jrest.devices.i2c.web;

import com.pi4j.io.i2c.I2CFactory;
import org.pi4jrest.devices.i2c.service.I2cDeviceManager;
import org.pi4jrest.devices.i2c.web.responses.I2cBoardResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helps to register and to remove i2c devices.
 */
@RestController
@RequestMapping("/boards/i2c/buses/")
public class I2cDeviceController {

    private final I2cDeviceManager i2cDeviceManager;

    public I2cDeviceController(I2cDeviceManager i2cDeviceManager) {
        this.i2cDeviceManager = i2cDeviceManager;
    }

    @PutMapping("{busNumber}")
    public void register(@PathVariable int busNumber) throws IOException, I2CFactory.UnsupportedBusNumberException {
        i2cDeviceManager.registerI2cDevice(busNumber);
    }

    @DeleteMapping("{busNumber}")
    public void remove(@PathVariable int busNumber) throws IOException, I2CFactory.UnsupportedBusNumberException {
        i2cDeviceManager.removeI2cDevice(busNumber);
    }

    @GetMapping
    public List<I2cBoardResponse> list() {
        return i2cDeviceManager.list()
                .stream()
                .map(board -> new I2cBoardResponse(1, board.getI2cDevice().getAddress())).collect(Collectors.toList());
    }
}
