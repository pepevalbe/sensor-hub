package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.Measurement;
import com.pepe.sensor.persistence.MeasurementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MeasurementRepository extends PagingAndSortingRepository<Measurement, String> {

	List<Measurement> findByUsernameAndTypeAndTimestampBetween(String username, MeasurementType type, long beginTimestamp, long endTimestamp);

	Page<Measurement> findByUsernameAndType(String username, MeasurementType type, Pageable pageable);

	void deleteByUsername(String username);
}
