package org.pi4jrest.motors.stepper.db.memory;

import org.pi4jrest.common.SerializationUtils;
import org.pi4jrest.motors.stepper.db.StepperMotorConnectionRepository;
import org.pi4jrest.motors.stepper.model.StepperMotorConnection;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@Component
public class StepperMotorConnectionInMemoryRepository implements StepperMotorConnectionRepository {

    private final Set<StepperMotorConnection> stepperMotors = new HashSet<>();

    @Override
    public Optional<StepperMotorConnection> get(String name) {
        return stepperMotors.stream()
                .filter(existingStepperMotor -> existingStepperMotor.getName().equalsIgnoreCase(name))
                .findAny();
    }

    @Override
    public StepperMotorConnection save(StepperMotorConnection stepperMotor) {
        boolean removed = stepperMotors.removeIf(nextStepperMotor -> nextStepperMotor.getName().equalsIgnoreCase(stepperMotor.getName()));
        stepperMotors.add(stepperMotor);
        return stepperMotor;
    }

    @Override
    public void delete(String stepperMotorName) {
        stepperMotors.removeIf(stepperMotor -> stepperMotor.getName().equalsIgnoreCase(stepperMotorName));
    }

    @Override
    public Collection<StepperMotorConnection> list() {
        return Collections.unmodifiableCollection(stepperMotors);
    }

    @PreDestroy
    public void serializeStepperMotors() {
        SerializationUtils.saveCollection(stepperMotors, "stepperMotors");
    }

    @PostConstruct
    public void initStepperMotors() {
        Collection<StepperMotorConnection> stepperMotors = SerializationUtils.readCollection(StepperMotorConnection.class, "stepperMotors");
        this.stepperMotors.addAll(stepperMotors);
    }
}
