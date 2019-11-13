package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

// Extends JpaRepository so we can use flush (see DataLoader)
@RepositoryRestResource
public interface PersonRepository extends JpaRepository<Person, String> {

	Optional<Person> findByToken(@Param("token") String token);

	Person findByEmail(@Param("email") String email);

	Long deleteByUsername(@Param("username") String username);
}
