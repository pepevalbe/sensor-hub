package com.pepe.sensor.service.mapper;

import com.pepe.sensor.dto.SensorReadingDTO;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.SensorReading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SensorReadingMapper {

    @Mapping(target = "token", ignore = true)
    SensorReadingDTO map(SensorReading sensorReading);

    SensorReading map(SensorReadingDTO sensorReadingDTO, Person owner);
}
