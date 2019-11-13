package com.pepe.sensor.service.mapper;

import com.pepe.sensor.dto.DoorEventDTO;
import com.pepe.sensor.persistence.DoorEvent;
import com.pepe.sensor.persistence.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoorEventMapper {

	@Mapping(target = "token", ignore = true)
	DoorEventDTO map(DoorEvent doorEvent);

	DoorEvent map(DoorEventDTO doorEventDTO, Person owner);
}
