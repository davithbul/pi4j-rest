package org.pi4jrest.motors.stepper.services;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.pi4jrest.motors.exceptions.MotorCurrentlyRunningException;
import org.pi4jrest.motors.exceptions.MotorNotFoundException;
import org.pi4jrest.motors.stepper.db.StepperMotorConnectionRepository;
import org.pi4jrest.motors.stepper.events.StepperMotorDeleteEvent;
import org.pi4jrest.motors.stepper.events.StepperMotorUpdateEvent;
import org.pi4jrest.motors.stepper.model.StepperMotor;
import org.pi4jrest.motors.stepper.model.StepperMotorConnection;
import org.pi4jrest.motors.stepper.model.pi4j.DefaultAsyncStepperMotor;
import org.pi4jrest.pins.services.facade.GpioPinServiceFacade;
import org.pi4jrest.pins.services.facade.PinDescriptionMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class StepperMotorConnectionService {

    private final Map<String, StepperMotor> stepperMotorMap = new HashMap<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ApplicationEventPublisher publisher;
    private final StepperMotorConnectionRepository motorDescriptionRepository;
    private final GpioPinServiceFacade gpioPinServiceFacade;
    private final PinDescriptionMapper pinDescriptionMapper;

    public StepperMotorConnectionService(ApplicationEventPublisher publisher, StepperMotorConnectionRepository motorDescriptionRepository, GpioPinServiceFacade gpioPinServiceFacade, PinDescriptionMapper pinDescriptionMapper) {
        this.publisher = publisher;
        this.motorDescriptionRepository = motorDescriptionRepository;
        this.gpioPinServiceFacade = gpioPinServiceFacade;
        this.pinDescriptionMapper = pinDescriptionMapper;
    }

    public synchronized void save(StepperMotorConnection motorDescription) {
        validatePins(motorDescription);
        if (stepperMotorMap.containsKey(motorDescription.getName())) {
            StepperMotor stepperMotor = stepperMotorMap.get(motorDescription.getName());
            if (stepperMotor != null && stepperMotor.getCurrentState().isRunning()) {
                throw new MotorCurrentlyRunningException(motorDescription.getName(), stepperMotor.getCurrentState());
            }

            stepperMotorMap.remove(motorDescription.getName());
            publisher.publishEvent(new StepperMotorUpdateEvent(motorDescription.getName()));
        }

        StepperMotor stepperMotor = initStepperMotor(motorDescription);
        motorDescriptionRepository.save(motorDescription);
        stepperMotorMap.put(motorDescription.getName(), stepperMotor);
    }

    public synchronized StepperMotorConnection getMotorDescription(String stepperMotorName) {
        Optional<StepperMotorConnection> motorDescriptionOptional = motorDescriptionRepository.get(stepperMotorName);
        return motorDescriptionOptional.orElseThrow(() -> new MotorNotFoundException(stepperMotorName));
    }


    public synchronized StepperMotor getStepperMotor(String stepperMotorName) {
        StepperMotor stepperMotor = stepperMotorMap.get(stepperMotorName);

        if (stepperMotor == null) {
            StepperMotorConnection motorDescription = getMotorDescription(stepperMotorName);
            stepperMotor = initStepperMotor(motorDescription);
            stepperMotorMap.put(stepperMotorName, stepperMotor);
        }

        return stepperMotor;
    }

    public synchronized void delete(String stepperMotorName) {
        StepperMotor stepperMotor = stepperMotorMap.get(stepperMotorName);
        if (stepperMotor != null && stepperMotor.getCurrentState().isRunning()) {
            throw new MotorCurrentlyRunningException(stepperMotorName, stepperMotor.getCurrentState());
        }

        stepperMotorMap.remove(stepperMotorName);

        Optional<StepperMotorConnection> motorDescriptionOptional = motorDescriptionRepository.get(stepperMotorName);
        if (!motorDescriptionOptional.isPresent()) {
            throw new MotorNotFoundException(stepperMotorName);
        }

        motorDescriptionRepository.delete(stepperMotorName);

        publisher.publishEvent(new StepperMotorDeleteEvent(stepperMotorName));
    }

    public Collection<StepperMotorConnection> listMotorDescriptions() {
        return motorDescriptionRepository.list();
    }

    private void validatePins(StepperMotorConnection motorPinMapping) {
        pinDescriptionMapper.validatePin(motorPinMapping.getPin1());
        pinDescriptionMapper.validatePin(motorPinMapping.getPin2());
        pinDescriptionMapper.validatePin(motorPinMapping.getPin3());
        pinDescriptionMapper.validatePin(motorPinMapping.getPin4());
    }

    private StepperMotor initStepperMotor(StepperMotorConnection motorPinMapping) {
        GpioPinDigitalOutput[] pins = {
                gpioPinServiceFacade.setupOutputPin(motorPinMapping.getPin1()),
                gpioPinServiceFacade.setupOutputPin(motorPinMapping.getPin2()),
                gpioPinServiceFacade.setupOutputPin(motorPinMapping.getPin3()),
                gpioPinServiceFacade.setupOutputPin(motorPinMapping.getPin4()),
        };

        return new DefaultAsyncStepperMotor(pins, executorService);
    }
}
