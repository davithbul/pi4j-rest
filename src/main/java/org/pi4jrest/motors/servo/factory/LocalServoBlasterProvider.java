package org.pi4jrest.motors.servo.factory;


import com.pi4j.component.servo.ServoDriver;
import com.pi4j.component.servo.ServoProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Implementation of https://github.com/richardghirst/PiBits/tree/master/ServoBlaster
 *
 * @author Daniel Sendula
 */
public class LocalServoBlasterProvider implements ServoProvider {

    // Default servo mapping of ServoBlaster's servod:
    //
    //    0 on P1-7           GPIO-4
    //    1 on P1-11          GPIO-17
    //    2 on P1-12          GPIO-18
    //    3 on P1-13          GPIO-27
    //    4 on P1-15          GPIO-22
    //    5 on P1-16          GPIO-23
    //    6 on P1-18          GPIO-24
    //    7 on P1-22          GPIO-25

    public static final String SERVO_BLASTER_DEV = "/dev/servoblaster";
    public static final String SERVO_BLASTER_DEV_CFG = "/dev/servoblaster-cfg";
    public static Map<Pin, String> PIN_MAP;
    public static Map<String, Pin> REVERSE_PIN_MAP;

    static {
        PIN_MAP = new HashMap<>();
        REVERSE_PIN_MAP = new HashMap<>();
        definePin(RaspiPin.GPIO_02, "P1-3");
        definePin(RaspiPin.GPIO_03, "P1-5");
        definePin(RaspiPin.GPIO_04, "P1-7");
        definePin(RaspiPin.GPIO_17, "P1-11");
        definePin(RaspiPin.GPIO_27, "P1-13");
        definePin(RaspiPin.GPIO_22, "P1-15");
        definePin(RaspiPin.GPIO_10, "P1-19");
        definePin(RaspiPin.GPIO_09, "P1-21");
        definePin(RaspiPin.GPIO_11, "P1-23");
        definePin(RaspiPin.GPIO_05, "P1-29");
        definePin(RaspiPin.GPIO_06, "P1-31");
        definePin(RaspiPin.GPIO_13, "P1-33");
        definePin(RaspiPin.GPIO_19, "P1-35");
        definePin(RaspiPin.GPIO_26, "P1-37");
    }

    protected File servoBlasterDev;
    protected File servoBlasterDevCfg;
    protected Writer writer;
    protected Map<Pin, LocalServoBlasterServoDriver> allocatedDrivers = new HashMap<>();

    /**
     * Constructor. It checks if /dev/servoblaster file exists.
     *
     * @throws IOException thrown in case file /dev/servoblaster does not exist.
     */
    public LocalServoBlasterProvider() throws IOException {
        servoBlasterDev = new File(SERVO_BLASTER_DEV);
        if (!servoBlasterDev.exists()) {
            throw new FileNotFoundException("File " + SERVO_BLASTER_DEV + " is not present." +
                    " Please check https://github.com/richardghirst/PiBits/tree/master/ServoBlaster for details.");
        }
        servoBlasterDevCfg = new File(SERVO_BLASTER_DEV_CFG);
        if (!servoBlasterDevCfg.exists()) {
            throw new FileNotFoundException("File " + SERVO_BLASTER_DEV_CFG + " is not present." +
                    " Please check https://github.com/richardghirst/PiBits/tree/master/ServoBlaster for details.");
        }

    }

    static void definePin(Pin pin, String s) {
        PIN_MAP.put(pin, s);
        REVERSE_PIN_MAP.put(s, pin);
    }

    public List<Pin> getDefinedServoPins() throws IOException {
        List<Pin> servoPins = new ArrayList<Pin>();
        FileReader in = new FileReader(servoBlasterDevCfg);
        try {
            @SuppressWarnings("unused")
            String p1pins = null;
            @SuppressWarnings("unused")
            String p5pins = null;
            boolean mappingStarted = false;

            @SuppressWarnings("resource")
            BufferedReader reader = new BufferedReader(in);

            String line = reader.readLine();
            while (line != null) {
                if (mappingStarted) {
                    line = line.trim();
                    int i = line.indexOf(" on ");
                    if (i > 0) {
                        try {
                            int index = Integer.parseInt(line.substring(0, i));
                            String pin = line.substring(i + 4).trim();
                            i = pin.indexOf(' ');
                            pin = pin.substring(0, i);

                            Pin gpio = REVERSE_PIN_MAP.get(pin);
                            if (gpio != null) {
                                if (index == servoPins.size()) {
                                    servoPins.add(gpio);
                                } else if (index > servoPins.size()) {
                                    while (servoPins.size() < index) {
                                        servoPins.add(null);
                                    }
                                    servoPins.add(gpio);
                                } else {
                                    servoPins.set(index, gpio);
                                }
                            } else {
                                System.err.println("Unrecognised pin " + pin);
                            }

                        } catch (NumberFormatException ignore) {
                        }
                    }
                } else {
                    if (line.startsWith("p1pins=")) {
                        p1pins = line.substring(7);
                    }
                    if (line.startsWith("p5pins=")) {
                        p5pins = line.substring(7);
                    }
                    if (line.trim().equals("Servo mapping:")) {
                        mappingStarted = true;
                    }
                }
                line = reader.readLine();
            }
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
            }
        }
        return servoPins;
    }

    /**
     * Returns new instance of {@link LocalServoBlasterServoDriver}.
     *
     * @param servoPin servo pin.
     * @return instance of {@link LocalServoBlasterServoDriver}.
     */
    public synchronized ServoDriver getServoDriver(Pin servoPin) throws IOException {
        List<Pin> servoPins = getDefinedServoPins();
        int index = servoPins.indexOf(servoPin);
        if (index < 0) {
            throw new IllegalArgumentException("Servo driver cannot drive pin " + servoPin);
        }

        LocalServoBlasterServoDriver driver = allocatedDrivers.get(servoPin);
        if (driver == null) {
            driver = new LocalServoBlasterServoDriver(servoPin, index, PIN_MAP.get(servoPin), this);
            ensureWriterIsCreated();
        }

        return driver;
    }

    protected synchronized void ensureWriterIsCreated() throws IOException {
        if (writer == null) {
            // Not really singleton, but it will work...
            writer = new FileWriter(servoBlasterDev);
        }
    }

    protected synchronized void updateServo(String pinName, int value) {
        StringBuilder b = new StringBuilder();
        b.append(pinName).append('=').append(Integer.toString(value)).append('\n');
        try {
            writer.write(b.toString());
            writer.flush();
        } catch (IOException e) {
            try {
                writer.close();
            } catch (IOException ignore) {
            }
        }
        try {
            ensureWriterIsCreated();
            writer.write(b.toString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to /dev/servoblaster device", e);
        }
    }
}
