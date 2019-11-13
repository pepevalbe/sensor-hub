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
public class SensorReadingRestController {

	public static final String PUBLIC_GENERIC_SENSOR_URL = "/public/genericsensor";
	private static final String USER_GENERIC_SENSOR_FIND_BY_DATE_URL = "/user/genericsensor/find";
	private static final String USER_GENERIC_SENSOR_FIND_BY_USERNAME_URL = "/user/genericsensor/findByUsername";
	private final MeasurementService measurementService;

	/**
	 * Create a Sensor Reading register
	 *
	 * @param measurementDto DTO object containing sensor reading and username
	 *                       timestamp will be ignored
	 * @return Resource just created
	 */
	@PostMapping(PUBLIC_GENERIC_SENSOR_URL)
	public ResponseEntity<MeasurementDto> post(@RequestBody MeasurementDto measurementDto) {

		return measurementService.create(measurementDto, MeasurementType.GENERIC)
				.map(t -> ResponseEntity.status(HttpStatus.CREATED).body(t))
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
	 * @return List of Sensor Reading with its Timestamps
	 */
	@GetMapping(USER_GENERIC_SENSOR_FIND_BY_DATE_URL)
	public ResponseEntity<List<MeasurementDto>> findByUsernameAndDate(Authentication auth,
																	  @RequestParam(value = "date", required = false) Date date,
																	  @RequestParam(value = "tz", defaultValue = "0") Integer tz) {

		return ResponseEntity.ok(measurementService.findByUsernameAndDate(auth.getName(),
				MeasurementType.GENERIC, new DateFilterDto(date, tz, 0)));
	}

	/**
	 * Find by username. Find all Sensor Reading registers
	 *
	 * @param auth Automatically filled when user is logged
	 * @param page Page number
	 * @param size Page size
	 * @return List of Sensor Reading (value 1, value2, value3) and Timestamp
	 */
	@GetMapping(USER_GENERIC_SENSOR_FIND_BY_USERNAME_URL)
	public ResponseEntity<Page<MeasurementDto>> findByUsername(Authentication auth,
															   @RequestParam(value = "page", defaultValue = "0") Integer page,
															   @RequestParam(value = "size", defaultValue = "20") Integer size) {

		if (size > 50) {
			size = 50;
		}

		return ResponseEntity.ok(measurementService.findByUsername(auth.getName(),
				MeasurementType.GENERIC, new PageDto(page, size)));
	}
}
