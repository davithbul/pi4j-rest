package org.pi4jrest.motors.servo.db;

import org.pi4jrest.common.SerializationUtils;
import org.pi4jrest.motors.servo.model.ServoMotorConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@Service
public class ServoMotorInMemoryRepo implements ServoMotorRepository {

    private final static Logger log = LoggerFactory.getLogger(ServoMotorInMemoryRepo.class);

    private final Set<ServoMotorConnection> servoMotors = new HashSet<>();

    @Override
    public Optional<ServoMotorConnection> get(String name) {
        return servoMotors.stream()
                .filter(existingServoMotor -> existingServoMotor.getName().equalsIgnoreCase(name))
                .findAny();
    }

    @Override
    public ServoMotorConnection save(ServoMotorConnection servoMotor) {
        boolean removed = servoMotors.removeIf(nextServoMotor -> nextServoMotor.getName().equalsIgnoreCase(servoMotor.getName()));
        log.debug("Saving servo motor: {}, existing: {}", servoMotor, removed);
        servoMotors.add(servoMotor);
        return servoMotor;
    }

    @Override
    public void delete(String servoMotorName) {
        log.debug("Deleting servo motor with name: {}", servoMotorName);
        servoMotors.removeIf(servoMotor -> servoMotor.getName().equalsIgnoreCase(servoMotorName));
    }

    @Override
    public Collection<ServoMotorConnection> list() {
        return Collections.unmodifiableCollection(servoMotors);
    }

    @PreDestroy
    public void serializeServoMotors() {
        log.debug("Serializing {} servo motors.", servoMotors.size());
        SerializationUtils.saveCollection(servoMotors, "servoMotors");
    }

    @PostConstruct
    public void initServoMotors() {
        Collection<ServoMotorConnection> servoMotors = SerializationUtils.readCollection(ServoMotorConnection.class, "servoMotors");
        this.servoMotors.addAll(servoMotors);
        log.debug("Restoring {} servo motors.", servoMotors.size());
    }
}
