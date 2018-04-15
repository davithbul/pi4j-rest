package org.pi4jrest.motors.servo.web;

import org.pi4jrest.motors.servo.model.ServoMotor;
import org.pi4jrest.motors.servo.model.ServoMotorConnection;
import org.pi4jrest.motors.servo.services.ServoMotorService;
import org.pi4jrest.motors.servo.web.dto.ServoMotorDTO;
import org.pi4jrest.motors.servo.web.dto.ServoMotorView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/motors/servos")
@CrossOrigin
public class RaspberryPiServoMotorController {

    private ServoMotorService servoMotorService;

    public RaspberryPiServoMotorController(ServoMotorService servoMotorService) {
        this.servoMotorService = servoMotorService;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.PUT)
    public void save(@PathVariable String name,
                     @RequestBody ServoMotorDTO servoMotorDTO) {
        ServoMotorConnection servoMotorConnection =
                new ServoMotorConnection(name, servoMotorDTO.getPin());
        servoMotorService.save(servoMotorConnection);
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String name) {
        servoMotorService.delete(name);
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ServoMotorView getStepperMotor(@PathVariable String name) {
        ServoMotorConnection motorDescription = servoMotorService.getMotorDescription(name);
        ServoMotor servoMotor = servoMotorService.getServoMotor(name);
        return new ServoMotorView(motorDescription, servoMotor.getCurrentState());
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<ServoMotorView> getStepperMotorViewList() {
        return servoMotorService.listMotorDescriptions().stream()
                .map(motorDescription ->
                        new ServoMotorView(
                                motorDescription,
                                servoMotorService.getServoMotor(motorDescription.getName()).getCurrentState()))
                .collect(Collectors.toList());
    }
}
