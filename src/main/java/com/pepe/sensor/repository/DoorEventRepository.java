package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.DoorEvent;
import com.pepe.sensor.persistence.Person;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DoorEventRepository extends PagingAndSortingRepository<DoorEvent, Long> {

    @Query("select d from DoorEvent d where d.owner = ?1 and d.timestamp > ?2 and d.timestamp < ?3 order by d.timestamp asc")
    List<DoorEvent> findByOwnerAndTimestampRange(Person owner, Timestamp beginTimestamp, Timestamp endTimestamp);

    @Query("select d from DoorEvent d where d.owner = (select p.id from Person p where p.username = ?1)")
    Page<DoorEvent> findByUsername(@Param("username") String username, Pageable pageable);

    List<DoorEvent> findByOwner(Person owner);
}
