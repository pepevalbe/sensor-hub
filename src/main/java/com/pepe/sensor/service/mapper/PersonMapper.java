package com.pepe.sensor.service.mapper;

import com.pepe.sensor.dto.PersonDTO;
import com.pepe.sensor.persistence.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDTO map(Person person);

    Person map(PersonDTO personDTO);
}
