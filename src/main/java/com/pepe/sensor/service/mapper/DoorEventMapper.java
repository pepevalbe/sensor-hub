package com.pepe.sensor.service.mapper;

import com.pepe.sensor.DTO.DoorEventDTO;
import com.pepe.sensor.persistence.DoorEvent;
import com.pepe.sensor.persistence.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoorEventMapper {
    
    DoorEventDTO map(DoorEvent doorEvent);

    DoorEvent map(DoorEventDTO doorEventDTO, Person owner);
}
