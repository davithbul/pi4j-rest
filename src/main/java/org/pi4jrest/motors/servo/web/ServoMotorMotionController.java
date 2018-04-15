package org.pi4jrest.motors.servo.web;

import org.pi4jrest.motors.exceptions.MotorStoppingException;
import org.pi4jrest.motors.servo.services.ServoMotorService;
import org.pi4jrest.motors.servo.web.dto.ServoMotorMotionRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/motors/servos/")
public class ServoMotorMotionController {

    private ServoMotorService servoMotorService;

    public ServoMotorMotionController(ServoMotorService servoMotorService) {
        this.servoMotorService = servoMotorService;
    }

    @RequestMapping(value = "{name}", method = RequestMethod.POST, params = {"motion"})
    public void move(@PathVariable String name,
                     @RequestBody ServoMotorMotionRequest servoMotorMotionRequest) throws InterruptedException {
        servoMotorService.getServoMotor(name).rotate(servoMotorMotionRequest.getDegree(), servoMotorMotionRequest.getSpeed());
    }

    @RequestMapping(value = "{name}", method = RequestMethod.PUT, params = "motion=stop")
    public void stop(@PathVariable String name) throws InterruptedException, MotorStoppingException {
        servoMotorService.getServoMotor(name).stop();
    }
}
