package com.pepe.sensor.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepe.sensor.ConfigVarHolder;
import com.pepe.sensor.controller.DoorEventRestController;
import com.pepe.sensor.controller.SensorReadingRestController;
import com.pepe.sensor.controller.TempHumidityRestController;
import com.pepe.sensor.service.UserService;
import com.pepe.sensor.service.dto.MeasurementDto;
import com.pepe.sensor.service.dto.TempHumidityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadLocalRandom;

/* Component responsible of performing http requests against the demo user account acting as a sensor */
@Service
public class DemoUserSensor {

	private final RestTemplate restTemplate;
	private final UserService userService;
	private final ConfigVarHolder configVarHolder;

	@Autowired
	public DemoUserSensor(RestTemplateBuilder restTemplateBuilder, UserService userService, ConfigVarHolder configVarHolder) {
		restTemplate = restTemplateBuilder.build();
		this.userService = userService;
		this.configVarHolder = configVarHolder;
	}

	@Async
	public void postTempHumidity() throws JsonProcessingException {

		if (configVarHolder.getWeatherUrl().isEmpty()) {
			return;
		}
		ResponseEntity<String> response = restTemplate.getForEntity(configVarHolder.getWeatherUrl(), String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode main = root.path("main");
		TempHumidityDto temphumidityDTO = new TempHumidityDto();
		temphumidityDTO.setTemperature(Float.parseFloat(main.path("temp").asText()));
		temphumidityDTO.setHumidity(Float.parseFloat(main.path("humidity").asText()));
		temphumidityDTO.setToken(userService.getUserByUsername("user").getToken());
		HttpEntity<TempHumidityDto> request = new HttpEntity<>(temphumidityDTO);
		restTemplate.postForObject(configVarHolder.getAppBaseUrl() + TempHumidityRestController.PUBLIC_TEMP_HUMIDITY_URL, request, TempHumidityDto.class);
	}

	@Async
	public void postDoorEvent() throws InterruptedException {
		Thread.sleep(ThreadLocalRandom.current().nextLong(1, 100) * 60 * 1000L);    // Sleeps 1-100 minutes
		MeasurementDto doorEventDTO = new MeasurementDto();
		doorEventDTO.setToken(userService.getUserByUsername("user").getToken());
		HttpEntity<MeasurementDto> request = new HttpEntity<>(doorEventDTO);
		restTemplate.postForObject(configVarHolder.getAppBaseUrl() + DoorEventRestController.PUBLIC_DOOR_EVENT_URL, request, MeasurementDto.class);
	}

	@Async
	public void postSensorReading() {
		MeasurementDto sensorReadingDTO = new MeasurementDto();
		sensorReadingDTO.setValue1(ThreadLocalRandom.current().nextFloat());
		sensorReadingDTO.setValue2((float) ThreadLocalRandom.current().nextDouble(-100, 100));
		sensorReadingDTO.setValue3((float) ThreadLocalRandom.current().nextGaussian());
		sensorReadingDTO.setToken(userService.getUserByUsername("user").getToken());
		HttpEntity<MeasurementDto> request = new HttpEntity<>(sensorReadingDTO);
		restTemplate.postForObject(configVarHolder.getAppBaseUrl() + SensorReadingRestController.PUBLIC_GENERIC_SENSOR_URL, request, MeasurementDto.class);
	}
}
