package com.pepe.sensor.service;

import com.pepe.sensor.dto.DateFilterDTO;
import com.pepe.sensor.dto.PageDTO;
import com.pepe.sensor.dto.SensorReadingDTO;
import com.pepe.sensor.persistence.SensorReading;
import com.pepe.sensor.repository.PersonRepository;
import com.pepe.sensor.repository.SensorReadingRepository;
import com.pepe.sensor.service.mapper.SensorReadingMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
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
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;

    private final PersonRepository personRepository;

    private final SensorReadingMapper sensorReadingMapper;

    @Transactional(readOnly = true)
    public Optional<SensorReadingDTO> getById(long id) {
        return sensorReadingRepository.findById(id).map(sensorReadingMapper::map);
    }

    @Transactional
    public void deleteById(long id) {
        sensorReadingRepository.deleteById(id);
    }

    @Transactional
    public Optional<SensorReadingDTO> create(@NonNull SensorReadingDTO sensorReadingDTO) {
        return personRepository.findByToken(sensorReadingDTO.getToken())
                .map(owner -> sensorReadingMapper.map(sensorReadingDTO, owner))
                .map(sensorReadingRepository::save)
                .map(sensorReadingMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<List<SensorReadingDTO>> find(String username, DateFilterDTO filter) {
        return personRepository.findById(username)
                .map(owner -> sensorReadingRepository.findByOwnerAndTimestampRange(owner, filter.getBegin(), filter.getEnd()).stream()
                        .map(sensorReadingMapper::map).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public Page<SensorReadingDTO> find(String username, PageDTO p) {
        return sensorReadingRepository.findByUsername(username, p.toRequest(Sort.Direction.ASC, "timestamp"))
                .map(sensorReadingMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<SensorReading> findAll(PageDTO p) {
        return sensorReadingRepository.findAll(p.toRequest(Sort.Direction.ASC, "timestamp"));
    }
}
