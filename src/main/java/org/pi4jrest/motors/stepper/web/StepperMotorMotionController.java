package org.pi4jrest.motors.stepper.web;

import org.pi4jrest.common.CollectionUtils;
import org.pi4jrest.motors.stepper.model.AsyncStepperMotor;
import org.pi4jrest.motors.stepper.model.StepperMotor;
import org.pi4jrest.motors.stepper.services.StepperMotorConnectionService;
import org.pi4jrest.motors.stepper.services.StepperMotorGroupConnectionService;
import org.pi4jrest.motors.stepper.web.requests.StepperMotorMotionRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/motors/steppers/")
public class StepperMotorMotionController {

    private StepperMotorConnectionService stepperMotorConnectionService;

    private StepperMotorGroupConnectionService stepperMotorGroupConnectionService;

    public StepperMotorMotionController(StepperMotorConnectionService stepperMotorConnectionService, StepperMotorGroupConnectionService stepperMotorGroupConnectionService) {
        this.stepperMotorConnectionService = stepperMotorConnectionService;
        this.stepperMotorGroupConnectionService = stepperMotorGroupConnectionService;
    }

    @RequestMapping(value = "{names}", method = RequestMethod.POST, params = "motion")
    public void move(@PathVariable Set<String> names,
                     @RequestBody StepperMotorMotionRequest motionRequest) throws InterruptedException {
        StepperMotor stepperMotor;
        if (names.size() > 1) {
            stepperMotor = stepperMotorGroupConnectionService.getGroupOfStepperMotors(names);
        } else {
            stepperMotor = stepperMotorConnectionService.getStepperMotor(names.iterator().next());
        }

        stepperMotor.step(motionRequest.getCount(), motionRequest.getStepType(), motionRequest.getStepInterval());
    }

    @RequestMapping(value = "{names}", method = RequestMethod.POST, params = {"motion", "async=true"})
    public void moveAsync(@PathVariable Set<String> names,
                          @RequestBody StepperMotorMotionRequest motionRequest) {
        AsyncStepperMotor stepperMotor;
        if (names.size() > 1) {
            stepperMotor = (AsyncStepperMotor) stepperMotorGroupConnectionService.getGroupOfStepperMotors(names);
        } else {
            stepperMotor = (AsyncStepperMotor) stepperMotorConnectionService.getStepperMotor(CollectionUtils.get(names, 0));
        }

        stepperMotor.asyncStep(motionRequest.getCount(), motionRequest.getStepType(), motionRequest.getStepInterval());
    }

    @RequestMapping(value = "{names}", method = RequestMethod.PUT, params = "motion=stop")
    public void stop(@PathVariable Set<String> names) {
        if (names.size() > 1) {
            stepperMotorGroupConnectionService.getGroupOfStepperMotors(names).stop();
        } else {
            stepperMotorConnectionService.getStepperMotor(CollectionUtils.get(names, 0)).stop();
        }
    }
}
