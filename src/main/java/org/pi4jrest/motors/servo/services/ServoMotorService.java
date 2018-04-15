package org.pi4jrest.motors.servo.services;

import org.pi4jrest.motors.servo.model.ServoMotor;
import org.pi4jrest.motors.servo.model.ServoMotorConnection;

import java.util.Collection;

public interface ServoMotorService {

    void save(ServoMotorConnection motorDescription);

    ServoMotorConnection getMotorDescription(String servoMotorName);

    ServoMotor getServoMotor(String servoMotorName);

    void delete(String servoMotorName);

    Collection<ServoMotorConnection> listMotorDescriptions();
}
