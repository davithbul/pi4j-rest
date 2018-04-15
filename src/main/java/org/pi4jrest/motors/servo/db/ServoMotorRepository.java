package org.pi4jrest.motors.servo.db;

import org.pi4jrest.motors.servo.model.ServoMotorConnection;

import java.util.Collection;
import java.util.Optional;

public interface ServoMotorRepository {

    Optional<ServoMotorConnection> get(String name);

    ServoMotorConnection save(ServoMotorConnection stepperMotor);

    void delete(String name);

    Collection<ServoMotorConnection> list();
}
