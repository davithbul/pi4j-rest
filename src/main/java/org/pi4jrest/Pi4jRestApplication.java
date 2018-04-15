package org.pi4jrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.StandardEnvironment;

import java.util.Objects;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

@SpringBootApplication
public class Pi4jRestApplication {

    public static void main(String[] args) {
        setActiveProfile();
        SpringApplication.run(Pi4jRestApplication.class, args);
    }

    private static void setActiveProfile() {
        String[] activeProfiles = new StandardEnvironment().getActiveProfiles();
        //if there is no profile set than use user to find out environment
        if (activeProfiles.length == 0) {
            String activeProfile = Objects.equals(System.getenv("USER"), "pi") ||
                    Objects.equals(System.getenv("SUDO_USER"), "pi") ? "pi" : "dev";
            System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, activeProfile);
        }
    }
}
