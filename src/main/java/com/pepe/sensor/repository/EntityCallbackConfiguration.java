package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.Measurement;
import com.pepe.sensor.persistence.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveCallback;

import java.util.UUID;

@Configuration
public class EntityCallbackConfiguration {

	@Bean
	BeforeSaveCallback<Person> beforeSavePerson() {
		return (entity, document, collection) -> {
			entity.setCreationTimestamp(System.currentTimeMillis());
			entity.setToken(UUID.randomUUID().toString());
			entity.setActivated(false);
			entity.setDoorRegisterActiveFlag(false);
			return entity;
		};
	}

	@Bean
	BeforeSaveCallback<Measurement> beforeSaveMeasurement() {
		return (entity, document, collection) -> {
			entity.setTimestamp(System.currentTimeMillis());
			return entity;
		};
	}
}

