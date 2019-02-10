package com.pepe.sensor.service.mapper;

import com.pepe.sensor.dto.TempHumidityDTO;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.TempHumidity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TempHumidityMapper {

    TempHumidityDTO map(TempHumidity tempHumidity);

    TempHumidity map(TempHumidityDTO tempHumidity, Person owner);
}
