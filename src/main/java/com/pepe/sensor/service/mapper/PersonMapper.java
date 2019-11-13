package com.pepe.sensor.service.mapper;

import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.service.dto.PersonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {

	@Mapping(target = "password", ignore = true)
	PersonDto map(Person person);

	Person map(PersonDto personDTO);
}
