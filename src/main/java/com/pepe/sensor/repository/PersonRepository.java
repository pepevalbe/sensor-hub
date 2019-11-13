package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PersonRepository extends MongoRepository<Person, String> {

	Optional<Person> findByToken(String token);

	Optional<Person> findByEmail(String email);

	void deleteByUsername(String username);
}
