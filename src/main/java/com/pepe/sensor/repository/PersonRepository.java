package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.query.Param;

// Extends JpaRepository so we can use flush (see DataLoader)
// Exported as HATEOAS in /profile
@RepositoryRestResource
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByUsername(@Param("username") String username);
    
    Person findByToken(@Param("token") String token);
    
    Person findByEmail(@Param("email") String email);
    
    Long deleteByUsername(@Param("username") String username);
}
