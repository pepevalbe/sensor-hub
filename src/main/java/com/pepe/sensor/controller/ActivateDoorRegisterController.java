package com.pepe.sensor.controller;

import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.repository.PersonRepository;
import java.security.Principal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/user")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ActivateDoorRegisterController {

    private PersonRepository personRepository;

    /**
     * Activate door access register. POST events will create registers
     *
     * @param principal Automatically filled when user is logged
     * @return 200 OK
     */
    @RequestMapping("/activate-doorevents")
    public ResponseEntity activate(Principal principal) {
        Person user = personRepository.getOne(principal.getName());
        user.setDoorRegisterActiveFlag(true);
        personRepository.save(user);
        log.info(user.getUsername() + " activated door register");
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Deactivate door access register. POST events will be ignored
     *
     * @param principal Automatically filled when user is logged
     * @return 200 OK
     */
    @RequestMapping("/deactivate-doorevents")
    public ResponseEntity deactivate(Principal principal) {
        Person user = personRepository.getOne(principal.getName());
        user.setDoorRegisterActiveFlag(false);
        personRepository.save(user);
        log.info(user.getUsername() + " deactivated door register");
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Check door access register status
     *
     * @param principal Automatically filled when user is logged
     * @return Door register status: 1 on, 0 off
     */
    @RequestMapping("/get-doorevents-status")
    public ResponseEntity<String> getStatus(Principal principal) {
        return personRepository.findById(principal.getName())
                .map((Person p) -> ResponseEntity.ok(String.valueOf(p.isDoorRegisterActiveFlag())))
                .get();
    }
}
