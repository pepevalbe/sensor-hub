package com.pepe.sensor.service.mapper;

import com.pepe.sensor.DTO.SensorReadingDTO;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.SensorReading;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SensorReadingMapper {

    SensorReadingDTO map(SensorReading sensorReading);

    SensorReading map(SensorReadingDTO sensorReadingDTO, Person owner);
}
