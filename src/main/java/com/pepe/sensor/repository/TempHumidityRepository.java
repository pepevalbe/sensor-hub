package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.TempHumidity;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TempHumidityRepository extends PagingAndSortingRepository<TempHumidity, Long> {

    @Query("select t from TempHumidity t where t.owner = ?1 and t.timestamp > ?2 and t.timestamp < ?3 order by t.timestamp asc")
    List<TempHumidity> findByOwnerAndTimestampRange(Person owner, Timestamp beginTimestamp, Timestamp endTimestamp);

    @Query("select t from TempHumidity t where t.owner = (select p.id from Person p where p.username = ?1)")
    Page<TempHumidity> findByUsername(@Param("username") String username, Pageable pageable);

    List<TempHumidity> findByOwner(@Param("owner") Person owner);
}
