package com.pepe.sensor.security;

import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private PersonRepository personRepository;

    @Autowired
    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DisabledException {
        Person user = personRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found!");
        }
        if (!user.isActivated()) {
            throw new DisabledException("User " + username + " not activated!");
        }

        UserDetails userDetails = User.withUsername(user.getUsername()).password(user.getPassword()).roles(user.getRole()).build();
        return userDetails;
    }
}
