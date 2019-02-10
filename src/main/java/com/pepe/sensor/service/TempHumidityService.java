package com.pepe.sensor.service;

import com.pepe.sensor.dto.TempHumidityDTO;
import com.pepe.sensor.dto.DateFilterDTO;
import com.pepe.sensor.dto.PageDTO;
import com.pepe.sensor.persistence.TempHumidity;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pepe.sensor.repository.TempHumidityRepository;
import com.pepe.sensor.repository.PersonRepository;
import com.pepe.sensor.service.mapper.TempHumidityMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class TempHumidityService {

    private TempHumidityRepository tempHumidityRepository;

    private PersonRepository personRepository;

    private TempHumidityMapper tempHumidityMapper;

    @Transactional(readOnly = true)
    public Optional<TempHumidityDTO> getById(long id) {
        return tempHumidityRepository.findById(id).map(tempHumidityMapper::map);
    }

    @Transactional
    public void deleteById(long id) {
        tempHumidityRepository.deleteById(id);
    }

    @Transactional
    public Optional<TempHumidityDTO> create(@NonNull TempHumidityDTO tempHumidityDTO) {
        return personRepository.findByToken(tempHumidityDTO.getToken())
                .map(owner -> tempHumidityMapper.map(tempHumidityDTO, owner))
                .map(tempHumidityRepository::save)
                .map(tempHumidityMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<List<TempHumidityDTO>> find(String username, DateFilterDTO filter) {
        return personRepository.findById(username)
                .map(owner -> tempHumidityRepository.findByOwnerAndTimestampRange(owner, filter.getBegin(), filter.getEnd()).stream()
                .map(tempHumidityMapper::map).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public Page<TempHumidityDTO> find(String username, PageDTO p) {
        return tempHumidityRepository.findByUsername(username, p.toRequest(Sort.Direction.ASC, "timestamp"))
                .map(tempHumidityMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<TempHumidity> findAll(PageDTO p) {
        return tempHumidityRepository.findAll(p.toRequest(Sort.Direction.ASC, "timestamp"));
    }
}
