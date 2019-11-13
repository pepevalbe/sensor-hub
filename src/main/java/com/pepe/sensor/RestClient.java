package com.pepe.sensor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepe.sensor.controller.DoorEventRestController;
import com.pepe.sensor.controller.SensorReadingRestController;
import com.pepe.sensor.controller.TempHumidityRestController;
import com.pepe.sensor.service.UserService;
import com.pepe.sensor.service.dto.MeasurementDto;
import com.pepe.sensor.service.dto.TempHumidityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class RestClient {

	private final RestTemplate restTemplate;
	private final UserService userService;

	@Value("${pepe-sensores.app_base_url}")
	private String APP_BASE_URL;

	@Value("${pepe-sensores.weather_url}")
	private String WEATHER_URL;

	@Autowired
	public RestClient(RestTemplateBuilder restTemplateBuilder, UserService userService, ProjectConstants projectConstants) {
		restTemplate = restTemplateBuilder.build();
		this.userService = userService;
	}

	@Async
	public void postTempHumidity() throws Exception {

		if (WEATHER_URL.isEmpty()) {
			return;
		}
		ResponseEntity<String> response = restTemplate.getForEntity(WEATHER_URL, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode main = root.path("main");
		TempHumidityDto temphumidityDTO = new TempHumidityDto();
		temphumidityDTO.setTemperature(Float.parseFloat(main.path("temp").asText()));
		temphumidityDTO.setHumidity(Float.parseFloat(main.path("humidity").asText()));
		temphumidityDTO.setToken(userService.getUserProfile("user").getToken());
		HttpEntity<TempHumidityDto> request = new HttpEntity<>(temphumidityDTO);
		restTemplate.postForObject(APP_BASE_URL + TempHumidityRestController.PUBLIC_TEMP_HUMIDITY_URL, request, TempHumidityDto.class);
	}

	@Async
	public void postDoorEvent() throws Exception {
		Thread.sleep(ThreadLocalRandom.current().nextLong(1, 100) * 60 * 1000L);    // Sleeps 1-100 minutes
		MeasurementDto doorEventDTO = new MeasurementDto();
		doorEventDTO.setToken(userService.getUserProfile("user").getToken());
		HttpEntity<MeasurementDto> request = new HttpEntity<>(doorEventDTO);
		restTemplate.postForObject(APP_BASE_URL + DoorEventRestController.PUBLIC_DOOR_EVENT_URL, request, MeasurementDto.class);
	}

	@Async
	public void postSensorReading() throws Exception {
		MeasurementDto sensorReadingDTO = new MeasurementDto();
		sensorReadingDTO.setValue1(ThreadLocalRandom.current().nextFloat());
		sensorReadingDTO.setValue2((float) ThreadLocalRandom.current().nextDouble(-100, 100));
		sensorReadingDTO.setValue3((float) ThreadLocalRandom.current().nextGaussian());
		sensorReadingDTO.setToken(userService.getUserProfile("user").getToken());
		HttpEntity<MeasurementDto> request = new HttpEntity<>(sensorReadingDTO);
		restTemplate.postForObject(APP_BASE_URL + SensorReadingRestController.PUBLIC_GENERIC_SENSOR_URL, request, MeasurementDto.class);
	}
}
