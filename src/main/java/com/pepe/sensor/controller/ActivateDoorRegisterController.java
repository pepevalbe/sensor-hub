package com.pepe.sensor.controller;

import com.pepe.sensor.service.ActivateDoorRegisterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController("/user")
@AllArgsConstructor
public class ActivateDoorRegisterController {

	private static final String API_DOOR_EVENT_ACTIVATE_URL = "/user/activate-doorevents";
	private static final String API_DOOR_EVENT_DEACTIVATE_URL = "/user/deactivate-doorevents";
	private static final String API_DOOR_EVENT_STATUS_URL = "/user/get-doorevents-status";

	private final ActivateDoorRegisterService activateDoorRegisterService;

	/**
	 * Activate door access register. POST events will create registers
	 *
	 * @param principal Automatically filled when user is logged
	 * @return 200 OK
	 */
	@RequestMapping(API_DOOR_EVENT_ACTIVATE_URL)
	public ResponseEntity<Void> activate(Principal principal) {

		activateDoorRegisterService.activate(principal.getName());
		return ResponseEntity.ok().build();
	}

	/**
	 * Deactivate door access register. POST events will be ignored
	 *
	 * @param principal Automatically filled when user is logged
	 * @return 200 OK
	 */
	@RequestMapping(API_DOOR_EVENT_DEACTIVATE_URL)
	public ResponseEntity<Void> deactivate(Principal principal) {

		activateDoorRegisterService.deactivate(principal.getName());
		return ResponseEntity.ok().build();
	}

	/**
	 * Check door access register status
	 *
	 * @param principal Automatically filled when user is logged
	 * @return Door register status: 1 on, 0 off
	 */
	@RequestMapping(API_DOOR_EVENT_STATUS_URL)
	public ResponseEntity<String> getStatus(Principal principal) {

		return ResponseEntity.ok(activateDoorRegisterService.status(principal.getName()));
	}
}
