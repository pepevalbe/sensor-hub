package com.pepe.sensor.controller;

import com.pepe.sensor.persistence.MeasurementType;
import com.pepe.sensor.service.MeasurementService;
import com.pepe.sensor.service.dto.DateFilterDto;
import com.pepe.sensor.service.dto.MeasurementDto;
import com.pepe.sensor.service.dto.PageDto;
import com.pepe.sensor.service.dto.TempHumidityDto;
import com.pepe.sensor.service.mapper.MeasurementMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class TempHumidityRestController {

	public static final String PUBLIC_TEMP_HUMIDITY_URL = "/public/temphumidity";
	private static final String USER_TEMP_HUMIDITY_FIND_BY_DATE_URL = "/user/temphumidity/find";
	private static final String USER_TEMP_HUMIDITY_FIND_BY_USERNAME_URL = "/user/temphumidity/findByUsername";

	private final MeasurementService measurementService;
	private final MeasurementMapper measurementMapper;

	/**
	 * Create a Temperature and Humidity register
	 *
	 * @param measurementDto DTO Object containing Temperature, Humidity and
	 *                       username timestamp will be ignored
	 * @return Resource just created
	 */
	@PostMapping(PUBLIC_TEMP_HUMIDITY_URL)
	public ResponseEntity<MeasurementDto> post(@RequestBody MeasurementDto measurementDto) {

		return measurementService.create(measurementDto, MeasurementType.TEMP_HUMIDITY)
				.map(t -> ResponseEntity.status(HttpStatus.CREATED).body(t))
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Find registers from logged user by date. If no date present, use server
	 * date
	 *
	 * @param auth   Automatically filled when user is logged
	 * @param filter date: Date to filter, format is: yyyy-mm-dd
	 * @param filter tz: Time zone difference, in minutes, from UTC to client locale
	 *               time. Default is 0 (UTC)
	 * @param filter minutes: Minutes offset to be added to date. Used to get data in
	 *               interval different than 00:00-23:59, for example 00:30-00:29 (minutes=30)
	 * @return List of Temperature, Humidity and Timestamp
	 */
	@GetMapping(USER_TEMP_HUMIDITY_FIND_BY_DATE_URL)
	public ResponseEntity<List<TempHumidityDto>> findByUsernameAndDate(Authentication auth, DateFilterDto filter) {
		return ResponseEntity.ok(
				measurementService.findByUsernameAndDate(auth.getName(), MeasurementType.TEMP_HUMIDITY, filter)
						.stream().map(measurementMapper::toTempHumidityDto).collect(Collectors.toList())
		);
	}

	/**
	 * Find by username. Find all Sensor Reading registers
	 *
	 * @param auth Automatically filled when user is logged
	 * @param page Page number
	 * @return Page of Temperature, Humidity and Timestamp from logged user
	 */
	@GetMapping(USER_TEMP_HUMIDITY_FIND_BY_USERNAME_URL)
	public ResponseEntity<Page<TempHumidityDto>> findByUsername(Authentication auth, PageDto page) {
		return ResponseEntity.ok(
				measurementService.findByUsername(auth.getName(), MeasurementType.TEMP_HUMIDITY, page)
						.map(measurementMapper::toTempHumidityDto)
		);
	}
}
