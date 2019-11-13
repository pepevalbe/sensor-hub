package com.pepe.sensor.controller;

import com.pepe.sensor.persistence.MeasurementType;
import com.pepe.sensor.service.MeasurementService;
import com.pepe.sensor.service.dto.DateFilterDto;
import com.pepe.sensor.service.dto.MeasurementDto;
import com.pepe.sensor.service.dto.PageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class DoorEventRestController {

	public static final String PUBLIC_DOOR_EVENT_URL = "/public/doorevent";
	private static final String USER_DOOR_EVENT_FIND_BY_DATE_URL = "/user/doorevent/find";
	private static final String USER_DOOR_EVENT_FIND_BY_USERNAME_URL = "/user/doorevent/findByUsername";

	private final MeasurementService measurementService;

	/**
	 * Create a Door Event register
	 *
	 * @param measurementDto DTO object containing username
	 * @return Resource just created
	 */
	@PostMapping(PUBLIC_DOOR_EVENT_URL)
	public ResponseEntity<MeasurementDto> post(@RequestBody MeasurementDto measurementDto) {

		return measurementService.create(measurementDto, MeasurementType.EVENT)
				.map(d -> ResponseEntity.status(HttpStatus.CREATED).body(d))
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Find registers from logged user by date. If no date present, use server
	 * date
	 *
	 * @param auth Automatically filled when user is logged
	 * @param date Date to filter, format is: yyyy-mm-dd
	 * @param tz   Time zone difference, in minutes, from UTC to client locale
	 *             time. Default is 0 (UTC)
	 * @return List of Door Events
	 */
	@GetMapping(USER_DOOR_EVENT_FIND_BY_DATE_URL)
	public ResponseEntity<List<MeasurementDto>> findByUsernameAndDate(Authentication auth,
																	  @RequestParam(value = "date", required = false) Date date,
																	  @RequestParam(value = "tz", defaultValue = "0") int tz) {

		return ResponseEntity.ok(measurementService.findByUsernameAndDate(auth.getName(),
				MeasurementType.EVENT, new DateFilterDto(date, tz, 0)));
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
	@GetMapping(USER_DOOR_EVENT_FIND_BY_USERNAME_URL)
	public ResponseEntity<Page<MeasurementDto>> findByUsername(Authentication auth,
															   @RequestParam(value = "page", defaultValue = "0") int page,
															   @RequestParam(value = "size", defaultValue = "20") int size) {

		return ResponseEntity.ok(measurementService.findByUsername(auth.getName(),
				MeasurementType.EVENT, new PageDto(page, size)));
	}
}
