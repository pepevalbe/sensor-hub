package com.pepe.sensor.controller;

import com.pepe.sensor.DTO.SensorReadingDTO;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.SensorReading;
import com.pepe.sensor.repository.PersonRepository;
import com.pepe.sensor.repository.SensorReadingRepository;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class SensorReadingRestController {

    public static final String USER_GENERICSENSOR_URL = "/user/genericsensor";
    public static final String PUBLIC_GENERICSENSOR_URL = "/public/genericsensor";
    public static final String USER_GENERICSENSOR_FINDBYDATE_URL = "/user/genericsensor/find";
    public static final String USER_GENERICSENSOR_FINDBYUSERNAME_URL = "/user/genericsensor/findByUsername";
    public static final String ADMIN_GENERICSENSOR_FINDALL_URL = "/admin/genericsensor/findall";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SensorReadingRepository sensorReadingRepository;

    @Autowired
    PersonRepository personRepository;

    /**
     * Get a Sensor Reading register
     *
     * @param id Register to get
     * @return
     */
    @GetMapping(USER_GENERICSENSOR_URL)
    public ResponseEntity<SensorReadingDTO> get(@RequestParam("id") long id) {
        return sensorReadingRepository.findById(id)
                .map(s -> ResponseEntity.ok(new SensorReadingDTO(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove a Sensor Reading register
     *
     * @param id Register to delete
     * @return Http 200 OK if deleted or 404 if not found
     */
    @DeleteMapping(USER_GENERICSENSOR_URL)
    public ResponseEntity delete(@RequestParam("id") long id) {

        if (sensorReadingRepository.existsById(id)) {
            sensorReadingRepository.deleteById(id);
            logger.trace("Sensor reading register deleted: " + id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a Sensor Reading register
     *
     * @param sensorReadingDTO DTO object containing sensor reading and username
     * timestamp will be ignored
     * @return Resource just created
     */
    @PostMapping(PUBLIC_GENERICSENSOR_URL)
    public ResponseEntity<SensorReadingDTO> post(@RequestBody SensorReadingDTO sensorReadingDTO) {

        // Search for user
        Optional<Person> opt = personRepository.findByToken(sensorReadingDTO.getToken());
        if (!opt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Person owner = opt.get();
        
        // Create new entity
        SensorReading sensorReading = new SensorReading(sensorReadingDTO.getValue1(), sensorReadingDTO.getValue2(), sensorReadingDTO.getValue3(), owner);
        SensorReading createdSensorReading = sensorReadingRepository.save(sensorReading);
        logger.trace(owner.getUsername() + " posted a sensor reading:" + createdSensorReading.toString());
        return new ResponseEntity<>(new SensorReadingDTO(createdSensorReading), HttpStatus.CREATED);
    }

    /**
     * Find registers from logged user by date. If no date present, use server
     * date
     *
     * @param auth Automatically filled when user is logged
     * @param date Date to filter, format is: yyyy-mm-dd
     * @param tz Time zone difference, in minutes, from UTC to client locale
     * time. Default is 0 (UTC)
     * @return List of Sensor Reading with its Timestamps
     */
    @GetMapping(USER_GENERICSENSOR_FINDBYDATE_URL)
    public ResponseEntity<List<SensorReadingDTO>> findByUsernameAndDate(Authentication auth,
            @RequestParam(value = "date", required = false) Date date,
            @RequestParam(value = "tz", defaultValue = "0") Integer tz) {

        List<SensorReading> sensorReadings;
        Person owner = personRepository.getOne(auth.getName());

        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        // Calculate beginning and end interval of the date in timestamp, considering timezone offset
        Timestamp beginTimestamp = new Timestamp(date.getTime() + tz * 60000);
        Timestamp endTimestamp = new Timestamp(beginTimestamp.getTime() + 86400000 - 1);

        // Get data from database
        sensorReadings = (List<SensorReading>) sensorReadingRepository.findByOwnerAndTimestampRange(owner, beginTimestamp, endTimestamp);
        logger.trace(owner.getUsername() + " requested some sensor readings in the interval: " + beginTimestamp + " - " + endTimestamp);

        // Convert data to DTO
        if (sensorReadings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<SensorReadingDTO> sensorReadingsDTO = new ArrayList<>();
            for (SensorReading sensorReading : sensorReadings) {
                sensorReadingsDTO.add(new SensorReadingDTO(sensorReading));
            }
            return new ResponseEntity<>(sensorReadingsDTO, HttpStatus.OK);
        }
    }

    /**
     * Find by username. Find all Sensor Reading registers
     *
     * @param auth Automatically filled when user is logged
     * @param page Page number
     * @param size Page size
     * @return List of Sensor Reading (value 1, value2, value3) and Timestamp
     */
    @GetMapping(USER_GENERICSENSOR_FINDBYUSERNAME_URL)
    public ResponseEntity<Page<SensorReadingDTO>> findByUsername(Authentication auth,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size) {

        if (size > 50) {
            size = 50;
        }

        Page<SensorReading> sensorReadings;
        sensorReadings = sensorReadingRepository.findByUsername(auth.getName(),
                new PageRequest(page, size, Sort.Direction.ASC, "timestamp"));
        logger.trace(auth.getName() + " requested some sensor readings (page,size): " + page + "," + size);

        Page<SensorReadingDTO> sensorReadingsDTO = sensorReadings.map(SensorReadingDTO::new);

        return new ResponseEntity<>(sensorReadingsDTO, HttpStatus.OK);
    }

    /**
     * Find all. Find all Sensor Reading registers
     *
     * @param page Page number
     * @param size Page size
     * @return Page of Sensor Reading (value 1, value2, value3) and Timestamp
     * from logged user
     */
    @GetMapping(ADMIN_GENERICSENSOR_FINDALL_URL)
    public ResponseEntity<Page<SensorReading>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size) {

        if (size > 50) {
            size = 50;
        }

        Page<SensorReading> sensorReadings;
        sensorReadings = sensorReadingRepository.findAll(new PageRequest(page, size, Sort.Direction.ASC, "timestamp"));
        logger.trace("Requested all sensor readings (page,size): " + page + "," + size);

        return new ResponseEntity<>(sensorReadings, HttpStatus.OK);
    }
}
