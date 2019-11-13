package com.pepe.sensor.service;

import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ActivateDoorRegisterService {

	private final PersonRepository personRepository;

	@Transactional()
	public void activate(String username) {

		Person user = personRepository.getOne(username);
		user.setDoorRegisterActiveFlag(true);
		personRepository.save(user);
		log.info(user.getUsername() + " activated door register");
	}

	@Transactional()
	public void deactivate(String username) {

		Person user = personRepository.getOne(username);
		user.setDoorRegisterActiveFlag(false);
		personRepository.save(user);
		log.info(user.getUsername() + " deactivated door register");
	}

	@Transactional(readOnly = true)
	public String status(String username) {

		boolean status = personRepository.findById(username)
				.map(Person::isDoorRegisterActiveFlag).get();

		return String.valueOf(status);
	}
}
