package com.pepe.sensor;

import com.pepe.sensor.persistence.ConfigVariable;
import com.pepe.sensor.repository.ConfigVariableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Helper class to get and keep project configuration constants.
 * They are looked up from different sources in the next order:
 * 1: JVM property passed from command line (-Dkey=value)
 * 2: Properties file (application.properties)
 * 3: Database (config_variable table)
 * 4: Operating System environment variable
 */
@Slf4j
@Component
public class ConfigVarHolder {

	public final String APP_BASE_URL;
	public final String EMAIL_USERNAME;
	public final String EMAIL_PASSWORD;
	public final String WEATHER_URL;

	private final Environment env;
	private final ConfigVariableRepository configVariableRepository;

	@Autowired
	public ConfigVarHolder(Environment env, ConfigVariableRepository configVariableRepository) {
		this.env = env;
		this.configVariableRepository = configVariableRepository;

		String retrievedAppBaseUrl = getConstant("APP_BASE_URL");
		// If app base url is not found we use heroku app name as workaround
		// This is needed for review apps in heroku (created on pull requests)
		APP_BASE_URL = retrievedAppBaseUrl != null ? retrievedAppBaseUrl : "http://" + getConstant("HEROKU_APP_NAME") + ".herokuapp.com";
		EMAIL_USERNAME = getConstant("EMAIL_USERNAME");
		EMAIL_PASSWORD = getConstant("EMAIL_PASSWORD");
		WEATHER_URL = getConstant("WEATHER_URL");
	}

	private String getConstant(String key) {
		String value;

		value = System.getProperty(key);
		if (value != null) {
			log.info("Configuration from JMV property: " + key + " = " + value);
			return value;
		}

		value = env.getProperty(key);
		if (value != null) {
			log.info("Configuration from properties file: " + key + " = " + value);
			return value;
		}

		value = configVariableRepository.findByVarKey(key).map(ConfigVariable::getVarValue).orElse(null);
		if (value != null) {
			log.info("Configuration from database: " + key + " = " + value);
			return value;
		}

		value = System.getenv(key);
		if (value != null) {
			log.info("Configuration from os enviroment variable: " + key + " = " + value);
			return value;
		}

		log.info("Couldn't find configuration value for: " + key);
		return value;
	}
}
