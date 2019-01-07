package com.pepe.sensor.service;

import com.pepe.sensor.DTO.DateFilterDTO;
import com.pepe.sensor.DTO.PageDTO;
import com.pepe.sensor.DTO.DoorEventDTO;
import com.pepe.sensor.persistence.DoorEvent;
import com.pepe.sensor.repository.PersonRepository;
import com.pepe.sensor.repository.DoorEventRepository;
import com.pepe.sensor.service.mapper.DoorEventMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoorEventService {

    @Autowired
    private DoorEventRepository doorEventRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DoorEventMapper doorEventMapper;

    @Transactional(readOnly = true)
    public Optional<DoorEventDTO> getById(long id) {
        return doorEventRepository.findById(id).map(doorEventMapper::map);
    }
    
    @Transactional
    public void deleteById(long id){
        doorEventRepository.deleteById(id);
    }
    
    @Transactional
    public Optional<DoorEventDTO> create(@NonNull DoorEventDTO doorEventDTO){
        return personRepository.findByToken(doorEventDTO.getToken())
                .map(owner -> doorEventMapper.map(doorEventDTO, owner))
                .map(doorEventRepository::save)
                .map(doorEventMapper::map);
    }
    
    @Transactional(readOnly = true)
    public Optional<List<DoorEventDTO>> find(String username, DateFilterDTO filter){
        return personRepository.findById(username)
                .map(owner -> doorEventRepository.findByOwnerAndTimestampRange(owner, filter.getBegin(), filter.getEnd()).stream()
                        .map(doorEventMapper::map).collect(Collectors.toList()));
    }
    
    @Transactional(readOnly = true)
    public Page<DoorEventDTO> find(String username, PageDTO p){
        return doorEventRepository.findByUsername(username, p.toRequest(Sort.Direction.ASC, "timestamp"))
                .map(doorEventMapper::map);
    }
    
    @Transactional(readOnly = true)
    public Page<DoorEvent> findAll(PageDTO p){
        return doorEventRepository.findAll(p.toRequest(Sort.Direction.ASC, "timestamp"));
    }
}
