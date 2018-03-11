package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.ConfigVariable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConfigVariableRepository extends CrudRepository<ConfigVariable, Long> {

    @Query("select x.varValue from ConfigVariable x where x.varKey = ?1")
    String getValueByKey(String varKey);
}
