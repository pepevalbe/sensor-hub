package com.pepe.sensor.service;

import com.pepe.sensor.persistence.MeasurementType;
import com.pepe.sensor.repository.MeasurementRepository;
import com.pepe.sensor.repository.PersonRepository;
import com.pepe.sensor.service.dto.DateFilterDto;
import com.pepe.sensor.service.dto.MeasurementDto;
import com.pepe.sensor.service.dto.PageDto;
import com.pepe.sensor.service.mapper.MeasurementMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MeasurementService {

	private final MeasurementRepository measurementRepository;
	private final PersonRepository personRepository;
	private final MeasurementMapper measurementMapper;

	@Transactional
	public Optional<MeasurementDto> create(MeasurementDto measurementDto, MeasurementType type) {
		return personRepository.findByToken(measurementDto.getToken())
				.map(person -> measurementMapper.map(measurementDto, type, person))
				.map(measurementRepository::save)
				.map(measurementMapper::map);
	}

	@Transactional(readOnly = true)
	public List<MeasurementDto> findByUsernameAndDate(String username, MeasurementType type, DateFilterDto filter) {
		return measurementRepository.findByUsernameAndTypeAndTimestampBetween(username, type, filter.getBegin(), filter.getEnd())
				.stream().map(measurementMapper::map).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<MeasurementDto> findByUsername(String username, MeasurementType type, PageDto p) {
		return measurementRepository.findByUsernameAndType(username, type, p.toRequest(Sort.Direction.ASC, "timestamp"))
				.map(measurementMapper::map);
	}

}
