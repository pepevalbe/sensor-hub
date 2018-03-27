package com.pepe.sensor.controller;

import com.pepe.sensor.DTO.TempHumidityDTO;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.TempHumidity;
import com.pepe.sensor.repository.PersonRepository;
import com.pepe.sensor.repository.TempHumidityRepository;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
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
public class TempHumidityRestController {

    public static final String USER_TEMPHUMIDITY_URL = "/user/temphumidity";
    public static final String PUBLIC_TEMPHUMIDITY_URL = "/public/temphumidity";
    public static final String USER_TEMPHUMIDITY_FINDBYDATE_URL = "/user/temphumidity/find";
    public static final String USER_TEMPHUMIDITY_FINDBYUSERNAME_URL = "/user/temphumidity/findByUsername";
    public static final String ADMIN_TEMPHUMIDITY_FINDALL_URL = "/admin/temphumidity/findall";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TempHumidityRepository tempHumidityRepository;

    @Autowired
    PersonRepository personRepository;

    /**
     * Get a Temperature and Humidity register
     *
     * @param id Register to get
     * @return
     */
    @GetMapping(USER_TEMPHUMIDITY_URL)
    public ResponseEntity<TempHumidityDTO> get(@RequestParam String id) {
        TempHumidity tempHumidity = tempHumidityRepository.findOne(new Long(id));
        if (tempHumidity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            logger.trace("TempHumidity register requested: " + tempHumidity.toString());
            return new ResponseEntity<>(new TempHumidityDTO(tempHumidity), HttpStatus.OK);
        }
    }

    /**
     * Remove a Temperature and Humidity register
     *
     * @param id Register to delete
     * @return Http 200 OK if deleted or 404 if not found
     */
    @DeleteMapping(USER_TEMPHUMIDITY_URL)
    public ResponseEntity delete(@RequestParam String id) {

        if (tempHumidityRepository.exists(new Long(id))) {
            tempHumidityRepository.delete(new Long(id));
            logger.trace("TempHumidity register deleted: " + id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a Temperature and Humidity register
     *
     * @param tempHumidityDTO DTO Object containing Temperature, Humidity and
     * username timestamp will be ignored
     * @return Resource just created
     */
    @PostMapping(PUBLIC_TEMPHUMIDITY_URL)
    public ResponseEntity<TempHumidityDTO> post(@RequestBody TempHumidityDTO tempHumidityDTO) {

        // Search for user
        Person owner = personRepository.findByToken(tempHumidityDTO.getToken());
        if (owner == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Create new entity
        TempHumidity tempHumidity = new TempHumidity(tempHumidityDTO.getTemperature(), tempHumidityDTO.getHumidity(), owner);
        TempHumidity createdTempHumidity = tempHumidityRepository.save(tempHumidity);
        logger.trace(owner.getUsername() + " posted a temphumidity register:" + createdTempHumidity.toString());
        return new ResponseEntity<>(new TempHumidityDTO(createdTempHumidity), HttpStatus.CREATED);
    }

    /**
     * Find registers from logged user by date. If no date present, use server
     * date
     *
     * @param auth Automatically filled when user is logged
     * @param date Date to filter, format is: yyyy-mm-dd
     * @param tz Time zone difference, in minutes, from UTC to client locale
     * time. Default is 0 (UTC)
     * @param minutes Minutes offset to be added to date. Used to get data in
     * interval different than 00:00-23:59, for example 00:30-00:29 (minutes=30)
     * @return List of Temperature, Humidity and Timestamp
     */
    @GetMapping(USER_TEMPHUMIDITY_FINDBYDATE_URL)
    public ResponseEntity<List<TempHumidityDTO>> findByUsernameAndDate(Authentication auth,
            @RequestParam(value = "date", required = false) Date date,
            @RequestParam(value = "tz", defaultValue = "0") Integer tz,
            @RequestParam(value = "minutes", defaultValue = "0") Integer minutes) {

        List<TempHumidity> tempHumidities;
        Person owner = personRepository.findByUsername(auth.getName());

        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        // Calculate beginning and end interval of the date in timestamp, considering timezone and minutes offset 
        Timestamp beginTimestamp = new Timestamp(date.getTime() + (tz + minutes) * 60000);
        Timestamp endTimestamp = new Timestamp(beginTimestamp.getTime() + 86400000 - 1);

        // Get data from database
        tempHumidities = (List<TempHumidity>) tempHumidityRepository.findByOwnerAndTimestampRange(owner, beginTimestamp, endTimestamp);
        logger.trace(owner.getUsername() + " requested some temhumidty registers in the interval: " + beginTimestamp + " - " + endTimestamp);

        // Convert data to DTO
        if (tempHumidities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<TempHumidityDTO> tempHumiditysDTO = new ArrayList<>();
            for (TempHumidity tempHumidity : tempHumidities) {
                tempHumiditysDTO.add(new TempHumidityDTO(tempHumidity));
            }
            return new ResponseEntity<>(tempHumiditysDTO, HttpStatus.OK);
        }
    }

    /**
     * Find by username. Find all Sensor Reading registers
     *
     * @param auth Automatically filled when user is logged
     * @param page Page number
     * @param size Page size
     * @return Page of Temperature, Humidity and Timestamp from logged user
     */
    @GetMapping(USER_TEMPHUMIDITY_FINDBYUSERNAME_URL)
    public ResponseEntity<Page<TempHumidityDTO>> findByUsername(Authentication auth,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size) {

        if (size > 50) {
            size = 50;
        }

        Page<TempHumidity> tempHumidities;
        tempHumidities = tempHumidityRepository.findByUsername(auth.getName(),
                new PageRequest(page, size, Sort.Direction.ASC, "timestamp"));
        logger.trace(auth.getName() + " requested some temphumidity registers (page,size): " + page + "," + size);

        Page<TempHumidityDTO> tempHumiditiesDTO = tempHumidities.map(new Converter<TempHumidity, TempHumidityDTO>() {
            @Override
            public TempHumidityDTO convert(TempHumidity tempHumidity) {
                return new TempHumidityDTO(tempHumidity);
            }
        });

        return new ResponseEntity<>(tempHumiditiesDTO, HttpStatus.OK);
    }

    /**
     * Find all. Find all Temperature and Humidity registers
     *
     * @param page Page number
     * @param size Page size
     * @return Page of Temperature, Humidity and Timestamp
     */
    @GetMapping(ADMIN_TEMPHUMIDITY_FINDALL_URL)
    public ResponseEntity<Page<TempHumidity>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size) {

        if (size > 50) {
            size = 50;
        }

        Page<TempHumidity> tempHumidities;
        tempHumidities = tempHumidityRepository.findAll(new PageRequest(page, size, Sort.Direction.ASC, "timestamp"));
        logger.trace("Requested all temphumidity registers (page,size): " + page + "," + size);

        return new ResponseEntity<>(tempHumidities, HttpStatus.OK);
    }
}
