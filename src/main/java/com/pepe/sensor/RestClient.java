package com.pepe.sensor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepe.sensor.dto.DoorEventDTO;
import com.pepe.sensor.dto.SensorReadingDTO;
import com.pepe.sensor.dto.TempHumidityDTO;
import com.pepe.sensor.controller.DoorEventRestController;
import com.pepe.sensor.controller.SensorReadingRestController;
import com.pepe.sensor.controller.TempHumidityRestController;
import com.pepe.sensor.repository.PersonRepository;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestClient {

    private final RestTemplate restTemplate;
    private final PersonRepository personRepository;

    @Value("${pepe-sensores.app_base_url}")
    private String APP_BASE_URL;

    @Value("${pepe-sensores.weather_url}")
    private String WEATHER_URL;

    @Autowired
    public RestClient(RestTemplateBuilder restTemplateBuilder,
            PersonRepository personRepository,
            ProjectConstants projectConstants) {
        restTemplate = restTemplateBuilder.build();
        this.personRepository = personRepository;
    }

    @Async
    public void postTempHumidity() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(WEATHER_URL, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode main = root.path("main");
        TempHumidityDTO temphumidityDTO = new TempHumidityDTO();
        temphumidityDTO.setTemperature(Float.parseFloat(main.path("temp").asText()));
        temphumidityDTO.setHumidity(Float.parseFloat(main.path("humidity").asText()));
        temphumidityDTO.setToken(personRepository.getOne("user").getToken());
        HttpEntity<TempHumidityDTO> request = new HttpEntity<>(temphumidityDTO);
        restTemplate.postForObject(APP_BASE_URL + TempHumidityRestController.PUBLIC_TEMPHUMIDITY_URL, request, TempHumidityDTO.class);
    }

    @Async
    public void postDoorEvent() throws Exception {
        Thread.sleep(ThreadLocalRandom.current().nextLong(1, 100) * 60 * 1000L);    // Seleeps 1-100 minutes
        DoorEventDTO doorEventDTO = new DoorEventDTO();
        doorEventDTO.setToken(personRepository.getOne("user").getToken());
        HttpEntity<DoorEventDTO> request = new HttpEntity<>(doorEventDTO);
        restTemplate.postForObject(APP_BASE_URL + DoorEventRestController.PUBLIC_DOOREVENT_URL, request, DoorEventDTO.class);
    }

    @Async
    public void postSensorReading() throws Exception {
        SensorReadingDTO sensorReadingDTO = new SensorReadingDTO();
        sensorReadingDTO.setValue1(ThreadLocalRandom.current().nextFloat());
        sensorReadingDTO.setValue2((float) ThreadLocalRandom.current().nextDouble(-100, 100));
        sensorReadingDTO.setValue3((float) ThreadLocalRandom.current().nextGaussian());
        sensorReadingDTO.setToken(personRepository.getOne("user").getToken());
        HttpEntity<SensorReadingDTO> request = new HttpEntity<>(sensorReadingDTO);
        restTemplate.postForObject(APP_BASE_URL + SensorReadingRestController.PUBLIC_GENERICSENSOR_URL, request, SensorReadingDTO.class);
    }
}
