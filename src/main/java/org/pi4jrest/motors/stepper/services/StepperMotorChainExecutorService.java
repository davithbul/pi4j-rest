package org.pi4jrest.motors.stepper.services;

import org.springframework.stereotype.Service;

/**
 * Executes stepper motor steps as a chain, it queues new requests
 * until before steps are not executed.
 */
@Service
public class StepperMotorChainExecutorService {

    private StepperMotorConnectionService stepperMotorConnectionService;

    public StepperMotorChainExecutorService(StepperMotorConnectionService stepperMotorConnectionService) {
        this.stepperMotorConnectionService = stepperMotorConnectionService;
    }

    public void executeNext() {

    }
}
