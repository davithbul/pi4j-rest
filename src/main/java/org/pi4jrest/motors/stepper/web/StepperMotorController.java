package org.pi4jrest.motors.stepper.web;

import org.pi4jrest.motors.stepper.model.StepperMotor;
import org.pi4jrest.motors.stepper.model.StepperMotorConnection;
import org.pi4jrest.motors.stepper.services.StepperMotorConnectionService;
import org.pi4jrest.motors.stepper.web.requests.StepperMotorRequest;
import org.pi4jrest.motors.stepper.web.responses.StepperMotorView;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/motors/steppers")
@CrossOrigin
public class StepperMotorController {

    private StepperMotorConnectionService stepperMotorConnectionService;

    public StepperMotorController(StepperMotorConnectionService stepperMotorConnectionService) {
        this.stepperMotorConnectionService = stepperMotorConnectionService;
    }

    @PutMapping(value = "/{name}")
    public void add(@PathVariable String name,
                    @RequestBody StepperMotorRequest stepperMotorRequest) {
        StepperMotorConnection stepperMotor = new StepperMotorConnection(name,
                stepperMotorRequest.getPin1(),
                stepperMotorRequest.getPin2(),
                stepperMotorRequest.getPin3(),
                stepperMotorRequest.getPin4());
        stepperMotorConnectionService.save(stepperMotor);
    }

    @DeleteMapping(value = "/{name}")
    public void delete(@PathVariable String name) {
        stepperMotorConnectionService.delete(name);
    }

    @GetMapping(value = "/{name}", params = "state=true")
    public StepperMotorView getStepperMotor(@PathVariable String name) {
        StepperMotorConnection motorDescription = stepperMotorConnectionService.getMotorDescription(name);
        StepperMotor stepperMotor = stepperMotorConnectionService.getStepperMotor(name);
        return new StepperMotorView(motorDescription, stepperMotor.getCurrentState());
    }

    @GetMapping(value = "/")
    public Collection<StepperMotorView> getStepperMotorViewList() {
        return stepperMotorConnectionService.listMotorDescriptions().stream()
                .map(motorDescription -> new StepperMotorView(motorDescription, stepperMotorConnectionService.getStepperMotor(motorDescription.getName()).getCurrentState()))
                .sorted(Comparator.comparing(StepperMotorView::getName))
                .collect(Collectors.toList());
    }
}
