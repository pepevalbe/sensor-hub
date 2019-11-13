package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.SensorReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.sql.Timestamp;
import java.util.List;

@RepositoryRestResource
public interface SensorReadingRepository extends PagingAndSortingRepository<SensorReading, Long> {

	@Query("select s from SensorReading s where s.owner = ?1 and s.timestamp > ?2 and s.timestamp < ?3 order by s.timestamp asc")
	List<SensorReading> findByOwnerAndTimestampRange(Person owner, Timestamp beginTimestamp, Timestamp endTimestamp);

	@Query("select s from SensorReading s where s.owner = (select p.id from Person p where p.username = ?1)")
	Page<SensorReading> findByUsername(@Param("username") String username, Pageable pageable);

	List<SensorReading> findByOwner(Person owner);
}
