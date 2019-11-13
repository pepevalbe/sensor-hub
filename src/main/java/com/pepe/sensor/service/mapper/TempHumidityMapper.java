package com.pepe.sensor.service.mapper;

import com.pepe.sensor.dto.TempHumidityDTO;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.TempHumidity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TempHumidityMapper {

	@Mapping(target = "token", ignore = true)
	TempHumidityDTO map(TempHumidity tempHumidity);

	TempHumidity map(TempHumidityDTO tempHumidity, Person owner);
}
