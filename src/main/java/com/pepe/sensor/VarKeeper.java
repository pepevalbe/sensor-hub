package com.pepe.sensor;

import com.pepe.sensor.persistence.ConfigVariable;
import com.pepe.sensor.repository.ConfigVariableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to manage project configuration variables.
 * On startup all configuration variables are read from the application property file to a map and saved in database
 * Reading is performed against the map, updating will update both the map and the database
 */
@Slf4j
@Component
public class VarKeeper {

	// Keys
	public static final String EMAIL_USERNAME_KEY = "spring.mail.username";
	public static final String EMAIL_PASSWORD_KEY = "spring.mail.password";
	public static final String APP_BASE_URL_KEY = "app_base_url";
	public static final String WEATHER_URL_KEY = "weather_url";
	public static final String USER_REGISTRY_ENABLED_KEY = "user_registry_enabled";
	public static final String DEMO_USER_KEY = "demo_user";

	// Configuration variables hash map
	private final Map<String, String> configVars;

	private final Environment env;
	private final ConfigVariableRepository configVariableRepository;


	public VarKeeper(Environment env, ConfigVariableRepository configVariableRepository) {
		this.env = env;
		this.configVariableRepository = configVariableRepository;
		configVars = new HashMap<>();

		configVars.put(EMAIL_USERNAME_KEY, initializeConfigurationVariable(EMAIL_USERNAME_KEY));
		configVars.put(EMAIL_PASSWORD_KEY, initializeConfigurationVariable(EMAIL_PASSWORD_KEY));
		configVars.put(APP_BASE_URL_KEY, initializeConfigurationVariable(APP_BASE_URL_KEY));
		configVars.put(WEATHER_URL_KEY, initializeConfigurationVariable(WEATHER_URL_KEY));
		configVars.put(USER_REGISTRY_ENABLED_KEY, initializeConfigurationVariable(USER_REGISTRY_ENABLED_KEY));
		configVars.put(DEMO_USER_KEY, initializeConfigurationVariable(DEMO_USER_KEY));
	}

	public Map<String, String> getAll() {

		return Collections.unmodifiableMap(configVars);
	}

	public String get(String key) {

		return configVars.get(key);
	}

	public void put(String key, String value) {

		log.info(String.format("Updating %s = %s", key, value));
		configVariableRepository.save(new ConfigVariable(key, value));
		configVars.put(key, value);
	}

	private String initializeConfigurationVariable(String key) {
		String value = env.getProperty(key);

		if (value != null) {
			configVariableRepository.save(new ConfigVariable(key, value));
			log.info("Successfully initialize configuration variable: " + key + " = " + value);
		} else {
			log.warn("Couldn't find configuration value for: " + key);
		}

		return value;
	}
}
