package com.pepe.sensor;

import com.pepe.sensor.repository.ConfigVariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Helper class to get and keep project configuration constants. They are looked
 * up from different sources in the next order:
 *
 * 1: JVM property passed from command line (-Dkey=value), 2: Properties file
 * (application.properties), 3: Database (config_variable table), 4: Operating
 * System enviroment variable
 */
@Component
public class ProjectConstants {

    public static String APP_BASE_URL;
    public static String EMAIL_USERNAME;
    public static String EMAIL_PASSWORD;
    public static String WEATHER_URL;

    private final Environment env;
    private final ConfigVariableRepository configVariableRepository;

    @Autowired
    public ProjectConstants(Environment env, ConfigVariableRepository configVariableRepository) {
        this.env = env;
        this.configVariableRepository = configVariableRepository;

        APP_BASE_URL = getConstant("APP_BASE_URL");
        // Workaround for review apps in heroku (created on pull requests)
        if (APP_BASE_URL == null ) {
            APP_BASE_URL = "http://" + getConstant("HEROKU_APP_NAME") + ".herokuapp.com";
        }
        EMAIL_USERNAME = getConstant("EMAIL_USERNAME");
        EMAIL_PASSWORD = getConstant("EMAIL_PASSWORD");
        WEATHER_URL = getConstant("WEATHER_URL");
    }

    private String getConstant(String key) {
        String value;

        value = System.getProperty(key);
        if (value != null) {
            System.out.println("Configuration from JMV property: " + key + " = " + value);
            return value;
        }

        value = env.getProperty(key);
        if (value != null) {
            System.out.println("Configuration from properties file: " + key + " = " + value);
            return value;
        }

        value = configVariableRepository.getValueByKey(key);
        if (value != null) {
            System.out.println("Configuration from database: " + key + " = " + value);
            return value;
        }

        value = System.getenv(key);
        if (value != null) {
            System.out.println("Configuration from os enviroment variable: " + key + " = " + value);
            return value;
        }

        System.out.println("Couldn't find configuration value for: " + key);
        return value;
    }
}
