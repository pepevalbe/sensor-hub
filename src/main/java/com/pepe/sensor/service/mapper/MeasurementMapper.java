package com.pepe.sensor.service.mapper;

import com.pepe.sensor.persistence.Measurement;
import com.pepe.sensor.persistence.MeasurementType;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.service.dto.MeasurementDto;
import com.pepe.sensor.service.dto.TempHumidityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeasurementMapper {

	@Mapping(source = "value1", target = "temperature")
	@Mapping(source = "value2", target = "humidity")
	@Mapping(target = "token", ignore = true)
	TempHumidityDto toTempHumidityDto(MeasurementDto measurementDto);


	@Mapping(target = "token", ignore = true)
	MeasurementDto map(Measurement measurement);

	Measurement map(MeasurementDto measurementDto, MeasurementType type, Person person);
}
