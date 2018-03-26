package com.pepe.sensor.controller;

import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.repository.PersonRepository;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ActivateDoorRegisterController {

    public static final String API_DOOREVENT_ACTIVATE_URL = "/user/activate-doorevents";
    public static final String API_DOOREVENT_DEACTIVATE_URL = "/user/deactivate-doorevents";
    public static final String API_DOOREVENT_STATUS_URL = "/user/get-doorevents-status";

    @Autowired
    PersonRepository personRepository;

    /**
     * Activate door access register. POST events will create registers
     *
     * @param principal Automatically filled when user is logged
     * @return 200 OK
     */
    @RequestMapping(API_DOOREVENT_ACTIVATE_URL)
    public ResponseEntity activate(Principal principal) {
        Person user = personRepository.findByUsername(principal.getName());
        user.setDoorRegisterActiveFlag(true);
        personRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Deactivate door access register. POST events will be ignored
     *
     * @param principal Automatically filled when user is logged
     * @return 200 OK
     */
    @RequestMapping(API_DOOREVENT_DEACTIVATE_URL)
    public ResponseEntity deactivate(Principal principal) {
        Person user = personRepository.findByUsername(principal.getName());
        user.setDoorRegisterActiveFlag(false);
        personRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Check door access register status
     *
     * @param principal Automatically filled when user is logged
     * @return Door register status: 1 on, 0 off
     */
    @RequestMapping(API_DOOREVENT_STATUS_URL)
    public ResponseEntity<String> getStatus(Principal principal) {
        Person user = personRepository.findByUsername(principal.getName());
        return new ResponseEntity<>(user.getDoorRegisterActiveFlag().toString(), HttpStatus.OK);
    }
}
