package com.pepe.sensor;

import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.repository.MeasurementRepository;
import com.pepe.sensor.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private MeasurementRepository measurementRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${pepe-sensores.demo_user_role}")
	private String demoUserRole;

	@Transactional
	@Override
	public void run(ApplicationArguments args) {

		// Create demo user
		Person user = new Person("user", passwordEncoder.encode("user"), demoUserRole,
				"user@email.com", "NombreDeUsuario", "ApellidoDeUsuario");

		// Remove any previously existing data of demo user
		measurementRepository.deleteByUsername(user.getUsername());
		personRepository.deleteByUsername(user.getUsername());

		// Create demo user (creationTimestamp and token are filled here)
		user = personRepository.save(user);

		// Activate demo user
		user.setActivated(true);
		personRepository.save(user);
	}
}
