package org.pi4jrest.motors.servo.services.prod;

import org.pi4jrest.motors.exceptions.MotorCurrentlyRunningException;
import org.pi4jrest.motors.exceptions.MotorNotFoundException;
import org.pi4jrest.motors.servo.db.ServoMotorRepository;
import org.pi4jrest.motors.servo.model.PigpioServoMotor;
import org.pi4jrest.motors.servo.model.ServoMotor;
import org.pi4jrest.motors.servo.model.ServoMotorConnection;
import org.pi4jrest.motors.servo.services.ServoMotorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Profile("!dev")
public class DefaultServoMotorService implements ServoMotorService {

    private final static Logger log = LoggerFactory.getLogger(DefaultServoMotorService.class);

    private final Map<String, ServoMotor> servoMotorMap = new HashMap<>();

    private ServoMotorRepository motorDescriptionRepository;

    public DefaultServoMotorService(ServoMotorRepository motorDescriptionRepository) {
        this.motorDescriptionRepository = motorDescriptionRepository;
    }

    public synchronized void save(ServoMotorConnection motorDescription) {
        if (servoMotorMap.containsKey(motorDescription.getName())) {
            ServoMotor servoMotor = servoMotorMap.get(motorDescription.getName());
            if (servoMotor != null && servoMotor.getCurrentState().isRunning()) {
                throw new MotorCurrentlyRunningException(
                        motorDescription.getName(),
                        servoMotor.getCurrentState()
                );
            }

            servoMotorMap.remove(motorDescription.getName());
        }

        motorDescriptionRepository.save(motorDescription);
        ServoMotor servoMotor = initServoMotor(motorDescription);
        servoMotorMap.put(motorDescription.getName(), servoMotor);
    }

    public synchronized ServoMotorConnection getMotorDescription(String servoMotorName) {
        Optional<ServoMotorConnection> motorDescriptionOptional = motorDescriptionRepository.get(servoMotorName);
        return motorDescriptionOptional.orElseThrow(() -> new MotorNotFoundException(servoMotorName));
    }


    public synchronized ServoMotor getServoMotor(String servoMotorName) {
        ServoMotor servoMotor = servoMotorMap.get(servoMotorName);

        if (servoMotor == null) {
            ServoMotorConnection motorDescription = getMotorDescription(servoMotorName);
            servoMotor = initServoMotor(motorDescription);
            servoMotorMap.put(servoMotorName, servoMotor);
        }

        return servoMotor;
    }

    public synchronized void delete(String servoMotorName) {
        ServoMotor servoMotor = servoMotorMap.get(servoMotorName);
        if (servoMotor != null && servoMotor.getCurrentState().isRunning()) {
            throw new MotorCurrentlyRunningException(servoMotorName, servoMotor.getCurrentState());
        }

        servoMotorMap.remove(servoMotorName);

        Optional<ServoMotorConnection> motorDescriptionOptional = motorDescriptionRepository.get(servoMotorName);
        if (!motorDescriptionOptional.isPresent()) {
            throw new MotorNotFoundException("Motor with name " + servoMotorName + " has not been found.");
        }

        motorDescriptionRepository.delete(servoMotorName);
    }

    public Collection<ServoMotorConnection> listMotorDescriptions() {
        return motorDescriptionRepository.list();
    }

    private PigpioServoMotor initServoMotor(ServoMotorConnection motorDescription) {

        //TODO: needs to be fixed for i2c
        return new PigpioServoMotor(motorDescription.getPin().getPinAddress());
    }

}
