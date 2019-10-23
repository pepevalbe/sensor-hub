package com.pepe.sensor.service;

import com.pepe.sensor.EmailSender;
import com.pepe.sensor.dto.PersonDTO;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.TemporaryToken;
import com.pepe.sensor.repository.PersonRepository;
import com.pepe.sensor.repository.TemporaryTokenRepository;
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

    private final TemporaryTokenRepository temporaryTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailSender emailSender;

    private final PersonMapper personMapper;

    @Transactional(readOnly = true)
    public PersonDTO getUserProfile(String username) {

        return personMapper.map(personRepository.findById(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public PersonDTO getByEmail(String email) {

        return personMapper.map(personRepository.findByEmail(email));
    }

    @Transactional()
    public void createUser(PersonDTO userDTO) {
        System.out.println(userDTO);
        Person user = personMapper.map(userDTO);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setTemporaryToken(new TemporaryToken(user));
        Person createdUser = personRepository.save(user);
        emailSender.sendActivateUserLinkEmail(createdUser);
        log.info("User created: " + createdUser.getUsername() + " - " + createdUser.getEmail());
    }

    @Transactional()
    public PersonDTO activateUser(String email, String token) {

        Person user = personRepository.findByEmail(email);

        // If token is ok we remove it and activate user otherwise return null to show an error
        if (user != null && user.getTemporaryToken() != null
                && token.equals(user.getTemporaryToken().getToken())
                && !user.getTemporaryToken().hasExpired()) {

            temporaryTokenRepository.delete(user.getTemporaryToken());
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

        Person user = personRepository.getOne(username);
        if (user != null) {
            user.setToken(UUID.randomUUID().toString());
            personRepository.save(user);
            emailSender.sendNewPersonalTokenEmail(user);
            log.info("Personal token regenerated: " + user.getUsername() + " - " + user.getEmail());
        }
    }

    @Transactional()
    public void generatePasswordToken(String email) {

        Person user = personRepository.findByEmail(email);

        if (user != null) {
            TemporaryToken previousToken = user.getTemporaryToken();
            if (previousToken != null) {
                // If there is a previous token, remove it
                temporaryTokenRepository.delete(previousToken);
            }
            // Create new token
            user.setTemporaryToken(new TemporaryToken(user));
            personRepository.save(user);
            emailSender.sendNewPasswordLinkEmail(user.getTemporaryToken());
        }
    }

    public boolean checkTemporaryToken(String email, String token) {

        Person user = personRepository.findByEmail(email);
        return user != null && user.getTemporaryToken() != null
                && token.equals(user.getTemporaryToken().getToken())
                && !user.getTemporaryToken().hasExpired();
    }

    public void resetPassword(String email, String token, String newPassword) {

        Person user = personRepository.findByEmail(email);

        // If token is ok we remove it and change password
        if (user != null && user.getTemporaryToken() != null
                && token.equals(user.getTemporaryToken().getToken())
                && !user.getTemporaryToken().hasExpired()) {

            temporaryTokenRepository.delete(user.getTemporaryToken());
            user.setTemporaryToken(null);
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setActivated(true);    // In case user vas never activated
            personRepository.save(user);
            log.info("Password reset: " + user.getUsername() + " - " + user.getEmail());
        }
    }

}
