package com.pepe.sensor.controller;

import com.pepe.sensor.DTO.DoorEventDTO;
import com.pepe.sensor.persistence.DoorEvent;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.repository.DoorEventRepository;
import com.pepe.sensor.repository.PersonRepository;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class DoorEventRestController {

    public static final String USER_DOOREVENT_URL = "/user/doorevent";
    public static final String PUBLIC_DOOREVENT_URL = "/public/doorevent";
    public static final String USER_DOOREVENT_FINDBYDATE_URL = "/user/doorevent/find";
    public static final String USER_DOOREVENT_FINDBYUSERNAME_URL = "/user/doorevent/findByUsername";
    public static final String ADMIN_DOOREVENT_FINDALL_URL = "/admin/doorevent/findall";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DoorEventRepository doorEventRepository;

    @Autowired
    PersonRepository personRepository;

    /**
     * Get a Door Event register
     *
     * @param id Register to get
     * @return
     */
    @GetMapping(USER_DOOREVENT_URL)
    public ResponseEntity<DoorEventDTO> get(@RequestParam("id") String id) {

        DoorEvent doorEvent = doorEventRepository.findOne(new Long(id));
        if (doorEvent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            logger.trace("Door event requested: " + doorEvent.toString());
            return new ResponseEntity<>(new DoorEventDTO(doorEvent), HttpStatus.OK);
        }
    }

    /**
     * Remove a Door Event register
     *
     * @param id Register to delete
     * @return Http 200 OK if deleted or 404 if not found
     */
    @DeleteMapping(USER_DOOREVENT_URL)
    public ResponseEntity delete(@RequestParam("id") String id) {

        if (doorEventRepository.exists(new Long(id))) {
            doorEventRepository.delete(new Long(id));
            logger.trace("Door event deleted: " + id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a Door Event register
     *
     * @param doorEventDTO DTO object containing username
     * @return Resource just created
     */
    @PostMapping(PUBLIC_DOOREVENT_URL)
    public ResponseEntity<DoorEventDTO> post(@RequestBody DoorEventDTO doorEventDTO) {
        // Search for user
        Person owner = personRepository.findByToken(doorEventDTO.getToken());
        if (owner == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // If user has disabled door register we ignore the request
        if (owner.getDoorRegisterActiveFlag() != true) {
            // return OK so the sensor doesn't keep trying to POST
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // Create new entity
        DoorEvent createdDoorEvent = doorEventRepository.save(new DoorEvent(owner));
        logger.trace(owner.getUsername() + " posted a new door event:" + createdDoorEvent.toString());
        return new ResponseEntity<>(new DoorEventDTO(createdDoorEvent), HttpStatus.CREATED);
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
            @RequestParam(value = "tz", defaultValue = "0") Integer tz) {

        List<DoorEvent> doorEvents;
        Person owner = personRepository.findByUsername(auth.getName());

        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        // Calculate beginning and end interval of the date in timestamp, considering timezone offset
        Timestamp beginTimestamp = new Timestamp(date.getTime() + tz * 60000);
        Timestamp endTimestamp = new Timestamp(beginTimestamp.getTime() + 86400000 - 1);

        // Get data from database        
        doorEvents = (List<DoorEvent>) doorEventRepository.findByOwnerAndTimestampRange(owner, beginTimestamp, endTimestamp);
        logger.trace(owner.getUsername() + " requested some door events in the interval: " + beginTimestamp + " - " + endTimestamp);

        // Convert data to DTO
        if (doorEvents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<DoorEventDTO> doorEventsDTO = new ArrayList<>();
            for (DoorEvent doorEvent : doorEvents) {
                doorEventsDTO.add(new DoorEventDTO(doorEvent));
            }
            return new ResponseEntity<>(doorEventsDTO, HttpStatus.OK);
        }
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
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size) {

        if (size > 50) {
            size = 50;
        }

        Page<DoorEvent> doorEvents;
        doorEvents = doorEventRepository.findByUsername(auth.getName(),
                new PageRequest(page, size, Sort.Direction.ASC, "timestamp"));
        logger.trace(auth.getName() + " requested some door events (page,size): " + page + "," + size);

        Page<DoorEventDTO> doorEventsDTO = doorEvents.map((DoorEvent doorEvent) -> new DoorEventDTO(doorEvent));

        return new ResponseEntity<>(doorEventsDTO, HttpStatus.OK);
    }

    /**
     * Find all. Find all Door Events registers
     *
     * @param page Page number
     * @param size Page size
     * @return List of Door Events and Timestamp
     */
    @RequestMapping(path = ADMIN_DOOREVENT_FINDALL_URL, method = GET)
    public ResponseEntity<Page<DoorEvent>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size) {

        if (size > 50) {
            size = 50;
        }

        Page<DoorEvent> doorEvents;
        doorEvents = doorEventRepository.findAll(new PageRequest(page, size, Sort.Direction.ASC, "timestamp"));
        logger.trace("Requested all door events (page,size): " + page + "," + size);
        return new ResponseEntity<>(doorEvents, HttpStatus.OK);
    }
}
