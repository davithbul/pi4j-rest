package org.pi4jrest.motors.stepper.services;

import org.pi4jrest.motors.stepper.events.StepperMotorDeleteEvent;
import org.pi4jrest.motors.stepper.events.StepperMotorUpdateEvent;
import org.pi4jrest.motors.stepper.model.StepperMotor;
import org.pi4jrest.motors.stepper.model.adapters.AdaptedStepperMotorComponent;
import org.pi4jrest.motors.stepper.model.pi4j.DefaultAsyncStepperMotorGroup;
import org.pi4jrest.motors.stepper.model.pi4j.DefaultStepperMotor;
import org.pi4jrest.motors.stepper.model.pi4j.StepperMotorGroup;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class StepperMotorGroupConnectionService {

    private final Map<Collection<String>, StepperMotorGroup> stepperMotorGroupMap = new HashMap<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private StepperMotorConnectionService stepperMotorConnectionService;

    public StepperMotorGroupConnectionService(StepperMotorConnectionService stepperMotorConnectionService) {
        this.stepperMotorConnectionService = stepperMotorConnectionService;
    }

    public synchronized StepperMotor getGroupOfStepperMotors(Set<String> stepperMotorNames) {
        if (stepperMotorNames.size() < 2) {
            throw new RuntimeException("Group should contain more than 1 stepper motor!");
        }

        if (stepperMotorGroupMap.containsKey(stepperMotorNames)) {
            return stepperMotorGroupMap.get(stepperMotorNames);
        }

        List<AdaptedStepperMotorComponent> motorComponents = stepperMotorNames
                .stream()
                .map(stepperMotorName -> ((DefaultStepperMotor) stepperMotorConnectionService.getStepperMotor(stepperMotorName)).getMotorComponent())
                .collect(Collectors.toList());

        DefaultAsyncStepperMotorGroup stepperMotorGroup = new DefaultAsyncStepperMotorGroup(motorComponents, executorService);

        stepperMotorGroupMap.put(stepperMotorNames, stepperMotorGroup);
        return stepperMotorGroup;
    }

    @EventListener
    public synchronized void handleStepperMotorDeletion(StepperMotorDeleteEvent stepperMotorDeleteEvent) {
        stepperMotorGroupMap.entrySet()
                .removeIf(collectionStepperMotorGroupEntry -> collectionStepperMotorGroupEntry.getKey().contains(stepperMotorDeleteEvent.getName()));
    }

    @EventListener
    public synchronized void handleStepperMotorUpdate(StepperMotorUpdateEvent stepperMotorUpdateEvent) {
        stepperMotorGroupMap.entrySet()
                .removeIf(collectionStepperMotorGroupEntry -> collectionStepperMotorGroupEntry.getKey().contains(stepperMotorUpdateEvent.getName()));
    }
}
