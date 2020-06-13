package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.Measurement;
import com.pepe.sensor.persistence.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;

import java.util.UUID;

@Configuration
public class EntityCallbackConfiguration {

	@Bean
	BeforeConvertCallback<Person> beforeSavePerson() {
		return (entity, document) -> {
			entity.setCreationTimestamp(System.currentTimeMillis());
			entity.setToken(UUID.randomUUID().toString());
			entity.setActivated(false);
			entity.setDoorRegisterActiveFlag(false);
			return entity;
		};
	}

	@Bean
	BeforeConvertCallback<Measurement> beforeConvertMeasurementCallback() {
		return (entity, document) -> {
			entity.setTimestamp(System.currentTimeMillis());
			return entity;
		};
	}
}

