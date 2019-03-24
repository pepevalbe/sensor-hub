package com.pepe.sensor.controller;

import com.pepe.sensor.dto.DateFilterDTO;
import com.pepe.sensor.dto.DoorEventDTO;
import com.pepe.sensor.dto.PageDTO;
import com.pepe.sensor.persistence.DoorEvent;
import com.pepe.sensor.service.DoorEventService;
import java.sql.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@AllArgsConstructor
public class DoorEventRestController {

    public static final String USER_DOOREVENT_URL = "/user/doorevent";
    public static final String PUBLIC_DOOREVENT_URL = "/public/doorevent";
    public static final String USER_DOOREVENT_FINDBYDATE_URL = "/user/doorevent/find";
    public static final String USER_DOOREVENT_FINDBYUSERNAME_URL = "/user/doorevent/findByUsername";
    public static final String ADMIN_DOOREVENT_FINDALL_URL = "/admin/doorevent/findall";

    private final DoorEventService doorEventService;

    /**
     * Get a Door Event register
     *
     * @param id Register to get
     * @return
     */
    @GetMapping(USER_DOOREVENT_URL)
    public ResponseEntity<DoorEventDTO> get(@RequestParam("id") long id) {

        return doorEventService.getById(id)
                .map(d -> ResponseEntity.ok(d))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove a Door Event register
     *
     * @param id Register to delete
     * @return Http 200 OK if deleted or 404 if not found
     */
    @DeleteMapping(USER_DOOREVENT_URL)
    public ResponseEntity delete(@RequestParam("id") long id) {

        doorEventService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Create a Door Event register
     *
     * @param doorEventDTO DTO object containing username
     * @return Resource just created
     */
    @PostMapping(PUBLIC_DOOREVENT_URL)
    public ResponseEntity<DoorEventDTO> post(@RequestBody DoorEventDTO doorEventDTO) {
   
        return doorEventService.create(doorEventDTO)
                .map(d -> ResponseEntity.status(HttpStatus.CREATED).body(d))
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
     * @return List of Door Events
     */
    @GetMapping(USER_DOOREVENT_FINDBYDATE_URL)
    public ResponseEntity<List<DoorEventDTO>> findByUsernameAndDate(Authentication auth,
            @RequestParam(value = "date", required = false) Date date,
            @RequestParam(value = "tz", defaultValue = "0") int tz) {

        return doorEventService.find(auth.getName(), new DateFilterDTO(date, tz, 0))
                .map(d -> ResponseEntity.ok(d))
                .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    /**
     * Find by username. Find all Door Events registers from logged user
     *
     * @param auth Automatically filled when user is logged
     * @param page Page number
     * @param size Page size
     * @return Page of Sensor Readings (value 1, value2, value3) and Timestamp
     * from logged user
     */
    @GetMapping(USER_DOOREVENT_FINDBYUSERNAME_URL)
    public ResponseEntity<Page<DoorEventDTO>> findByUsername(Authentication auth,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        return ResponseEntity.ok(doorEventService.find(auth.getName(), new PageDTO(page, size)));
    }

    /**
     * Find all. Find all Door Events registers
     *
     * @param page Page number
     * @param size Page size
     * @return List of Door Events and Timestamp
     */
    @RequestMapping(path = ADMIN_DOOREVENT_FINDALL_URL, method = GET)
    public ResponseEntity<Page<DoorEvent>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        return ResponseEntity.ok(doorEventService.findAll(new PageDTO(page, size)));
    }
}
