package com.pepe.sensor.controller;

import com.pepe.sensor.dto.DateFilterDTO;
import com.pepe.sensor.dto.PageDTO;
import com.pepe.sensor.dto.SensorReadingDTO;
import com.pepe.sensor.persistence.SensorReading;
import com.pepe.sensor.repository.PersonRepository;
import com.pepe.sensor.service.SensorReadingService;
import java.sql.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    SensorReadingService sensorReadingService;

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
        return sensorReadingService.getById(id)
                .map(t -> ResponseEntity.ok(t))
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

        sensorReadingService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
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

        return sensorReadingService.create(sensorReadingDTO)
                .map(t -> ResponseEntity.status(HttpStatus.CREATED).body(t))
                .orElse(ResponseEntity.notFound().build());
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

        return sensorReadingService.find(auth.getName(), new DateFilterDTO(date, tz, 0))
                .map(l -> ResponseEntity.ok(l))
                .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
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

        return ResponseEntity.ok(sensorReadingService.find(auth.getName(), new PageDTO(page, size)));
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

        return ResponseEntity.ok(sensorReadingService.findAll(new PageDTO(page, size)));
    }
}
