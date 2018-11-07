package com.pepe.sensor.controller;

import com.pepe.sensor.DTO.TempHumidityDTO;
import com.pepe.sensor.DTO.DateFilterDTO;
import com.pepe.sensor.DTO.PageDTO;
import com.pepe.sensor.persistence.TempHumidity;
import com.pepe.sensor.repository.PersonRepository;
import com.pepe.sensor.service.TempHumidityService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class TempHumidityRestController {

    public static final String USER_TEMPHUMIDITY_URL = "/user/temphumidity";
    public static final String PUBLIC_TEMPHUMIDITY_URL = "/public/temphumidity";
    public static final String USER_TEMPHUMIDITY_FINDBYDATE_URL = "/user/temphumidity/find";
    public static final String USER_TEMPHUMIDITY_FINDBYUSERNAME_URL = "/user/temphumidity/findByUsername";
    public static final String ADMIN_TEMPHUMIDITY_FINDALL_URL = "/admin/temphumidity/findall";
    
    private TempHumidityService tempHumidityService;

    private PersonRepository personRepository;

    /**
     * Get a Temperature and Humidity register
     *
     * @param id Register to get
     * @return
     */
    @GetMapping(USER_TEMPHUMIDITY_URL)
    public ResponseEntity<TempHumidityDTO> get(@RequestParam("id") long id) {
        return tempHumidityService.getById(id)
                .map(t -> ResponseEntity.ok(t))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove a Temperature and Humidity register
     *
     * @param id Register to delete
     * @return Http 200 OK if deleted or 404 if not found
     */
    @DeleteMapping(USER_TEMPHUMIDITY_URL)
    public ResponseEntity<String> delete(@RequestParam("id") long id) {
        tempHumidityService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
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
        return tempHumidityService.create(tempHumidityDTO)
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
     * @param minutes Minutes offset to be added to date. Used to get data in
     * interval different than 00:00-23:59, for example 00:30-00:29 (minutes=30)
     * @return List of Temperature, Humidity and Timestamp
     */
    @GetMapping(USER_TEMPHUMIDITY_FINDBYDATE_URL)
    public ResponseEntity<List<TempHumidityDTO>> findByUsernameAndDate(Authentication auth, DateFilterDTO filter) {
        return tempHumidityService.find(auth.getName(), filter)
                .map(l -> ResponseEntity.ok(l))
                .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    /**
     * Find by username. Find all Sensor Reading registers
     *
     * @param auth Automatically filled when user is logged
     * @param page Page number
     * @return Page of Temperature, Humidity and Timestamp from logged user
     */
    @GetMapping(USER_TEMPHUMIDITY_FINDBYUSERNAME_URL)
    public ResponseEntity<Page<TempHumidityDTO>> findByUsername(Authentication auth, PageDTO page) {
        return ResponseEntity.ok(tempHumidityService.find(auth.getName(), page));
    }

    /**
     * Find all. Find all Temperature and Humidity registers
     *
     * @param page Page
     * @return Page of Temperature, Humidity and Timestamp
     */
    @GetMapping(ADMIN_TEMPHUMIDITY_FINDALL_URL)
    public ResponseEntity<Page<TempHumidity>> findAll(PageDTO page) {
        return ResponseEntity.ok(tempHumidityService.findAll(page));
    }
}
