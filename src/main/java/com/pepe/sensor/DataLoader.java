package com.pepe.sensor;

import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.repository.PersonRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    PersonRepository personRepository;
    PasswordEncoder passwordEncoder;

    @Value("${pepe-sensores.demo_user_role}")
    String DEMO_USER_ROLE;

    @Autowired
    public DataLoader(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(ApplicationArguments args) {

        // Create demo user
        Person user = new Person("user", passwordEncoder.encode("user"), DEMO_USER_ROLE,
                "user@email.com", "NombreDeUsuario", "ApellidoDeUsuario");

        // Remove existing demo user (delete propagates to OnetToMany relationships)
        personRepository.deleteByUsername(user.getUsername());
        //Before saving we need to actually delete from database (flush)
        personRepository.flush();
        personRepository.saveAndFlush(user);

        // Activate demo user
        user.setActivated(true);
        personRepository.saveAndFlush(user);
    }
}
