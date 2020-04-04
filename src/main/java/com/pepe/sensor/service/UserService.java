package com.pepe.sensor.service;

import com.pepe.sensor.EmailSender;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.TemporaryToken;
import com.pepe.sensor.repository.PersonRepository;
import com.pepe.sensor.service.dto.PersonDto;
import com.pepe.sensor.service.mapper.PersonMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

	private final PersonRepository personRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailSender emailSender;
	private final PersonMapper personMapper;

	@Transactional(readOnly = true)
	public PersonDto getUserByUsername(String username) {

		return personMapper.map(personRepository.findById(username).orElse(null));
	}

	@Transactional(readOnly = true)
	public PersonDto getUserByEmail(String email) {

		return personMapper.map(personRepository.findByEmail(email).orElse(null));
	}

	@Transactional()
	public void createUser(PersonDto personDTO) {
		Person user = personMapper.map(personDTO);
		user.setRole("USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setTemporaryToken(new TemporaryToken());
		Person createdUser = personRepository.save(user);
		emailSender.sendActivateUserLinkEmail(createdUser);
		log.info("User created: " + createdUser.getUsername() + " - " + createdUser.getEmail());
	}

	@Transactional()
	public void modifyUser(PersonDto personDTO) {
		Person user = personRepository.findByEmail(personDTO.getEmail()).orElse(null);
		user.setUsername(personDTO.getUsername());
		user.setFirstName(personDTO.getFirstName());
		user.setLastName(personDTO.getLastName());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		personRepository.save(user);
		// TODO: send email
		log.info("User modified: " + user.getUsername() + " - " + user.getEmail());
	}

	@Transactional()
	public PersonDto activateUser(String email, String token) {

		Person user = personRepository.findByEmail(email).orElse(null);

		// If token is ok we clear it and activate user otherwise return null to show an error
		if (user != null && user.getTemporaryToken() != null
				&& token.equals(user.getTemporaryToken().getToken())
				&& !user.getTemporaryToken().hasExpired()) {

			user.setTemporaryToken(null);
			user.setActivated(true);
			personRepository.save(user);

			emailSender.sendWelcomeEmail(user);
			log.info("User activated: " + user.getUsername() + " - " + user.getEmail());

			return personMapper.map(user);
		}

		return null;
	}

	@Transactional()
	public void regeneratePersonalToken(String username) {

		personRepository.findById(username).ifPresent(person -> {
			person.setToken(UUID.randomUUID().toString());
			personRepository.save(person);
			emailSender.sendNewPersonalTokenEmail(person);
			log.info("Personal token regenerated: " + person.getUsername() + " - " + person.getEmail());
		});
	}

	@Transactional()
	public void generatePasswordToken(String email) {

		personRepository.findByEmail(email).ifPresent(person -> {
			// Create new temporary token
			person.setTemporaryToken(new TemporaryToken());
			personRepository.save(person);
			emailSender.sendNewPasswordLinkEmail(person);
		});
	}

	@Transactional(readOnly = true)
	public boolean checkTemporaryToken(String email, String token) {

		Person person = personRepository.findByEmail(email).orElse(null);
		return person != null && person.getTemporaryToken() != null
				&& token.equals(person.getTemporaryToken().getToken())
				&& !person.getTemporaryToken().hasExpired();
	}

	@Transactional()
	public void resetPassword(String email, String token, String newPassword) {

		personRepository.findByEmail(email).ifPresent(person -> {

			if (person.getTemporaryToken() != null
					&& token.equals(person.getTemporaryToken().getToken())
					&& !person.getTemporaryToken().hasExpired()) {
				// If token is ok we clear it and change password
				person.setTemporaryToken(null);
				person.setPassword(passwordEncoder.encode(newPassword));
				person.setActivated(true);    // In case user vas never activated
				personRepository.save(person);
				log.info("Password reset: " + person.getUsername() + " - " + person.getEmail());
			}
		});
	}

}
