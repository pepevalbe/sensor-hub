package com.pepe.sensor;

import com.pepe.sensor.persistence.ConfigVariable;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.repository.ConfigVariableRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Helper class manage project configuration variables. All components should used this to load any configuration
 * Variables are looked up on startup from different sources in the next order:
 * 1: JVM property passed from command line (-Dkey=value)
 * 2: Property file (application.properties)
 * 3: Database (config_variable table)
 * 4: Operating System environment variable
 */
@Slf4j
@Data
@Component
public class ConfigVarHolder {

	private final String emailUsername;
	private final String emailPassword;
	private final String userRegistryEnabled;
	private final String appBaseUrl;
	private final String weatherUrl;
	private final Person demoUser;

	private final Environment env;
	private final ConfigVariableRepository configVariableRepository;

	@Autowired
	public ConfigVarHolder(Environment env, ConfigVariableRepository configVariableRepository) {
		this.env = env;
		this.configVariableRepository = configVariableRepository;

		emailUsername = getConstant("spring.mail.username");
		emailPassword = getConstant("spring.mail.password");
		userRegistryEnabled = getConstant("user_registry_enabled");
		appBaseUrl = getConstant("app_base_url");
		weatherUrl = getConstant("weather_url");
		demoUser = parseDemoUser(getConstant("demo_user"));
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
			log.info("Configuration from property file: " + key + " = " + value);
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

	private Person parseDemoUser(String serializedDemoUser) {

		if (serializedDemoUser == null || serializedDemoUser.split(",").length != 6) {
			throw new RuntimeException("Can not parse demo user!");
		}

		String[] demoUserFields = serializedDemoUser.split(",");

		return new Person(demoUserFields[0], demoUserFields[1], demoUserFields[2], demoUserFields[3], demoUserFields[4], demoUserFields[5]);
	}
}
