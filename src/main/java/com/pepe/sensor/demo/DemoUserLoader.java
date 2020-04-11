package com.pepe.sensor.demo;

import com.pepe.sensor.VarKeeper;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.repository.MeasurementRepository;
import com.pepe.sensor.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.pepe.sensor.VarKeeper.DEMO_USER_KEY;

/* Component responsible of loading demo user on startup */
@Component
public class DemoUserLoader implements ApplicationRunner {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private MeasurementRepository measurementRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private VarKeeper varKeeper;

	@Transactional
	@Override
	public void run(ApplicationArguments args) {

		// Retrieve demo user from configuration
		Person demoUser = parseDemoUser(varKeeper.get(DEMO_USER_KEY));

		// Encode demo user password before persisting
		demoUser.setPassword(passwordEncoder.encode(demoUser.getPassword()));

		// Remove any previously existing data of demo user
		measurementRepository.deleteByUsername(demoUser.getUsername());
		personRepository.deleteByUsername(demoUser.getUsername());

		// Create demo user (creationTimestamp and token are filled here)
		demoUser = personRepository.save(demoUser);

		// Activate demo user
		demoUser.setActivated(true);

		// Enable door event registers
		demoUser.setDoorRegisterActiveFlag(true);

		personRepository.save(demoUser);
	}

	private Person parseDemoUser(String serializedDemoUser) {

		if (serializedDemoUser == null || serializedDemoUser.split(",").length != 6) {
			throw new RuntimeException("Can not parse demo user!");
		}

		String[] demoUserFields = serializedDemoUser.split(",");

		return new Person(demoUserFields[0], demoUserFields[1], demoUserFields[2], demoUserFields[3], demoUserFields[4], demoUserFields[5]);
	}
}
