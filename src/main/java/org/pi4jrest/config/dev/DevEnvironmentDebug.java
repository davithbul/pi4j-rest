package org.pi4jrest.config.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Profile("dev")
@Service
public class DevEnvironmentDebug {

    private final static Logger log = LoggerFactory.getLogger(DevEnvironmentDebug.class);

    @PostConstruct
    private static void printEnv() {
        log.info("-------------------------------------------\n\n");
        log.info("Welcome dev env!");
        log.info("-------------------------------------------\n\n");
    }
}
