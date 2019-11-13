package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.ConfigVariable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConfigVariableRepository extends MongoRepository<ConfigVariable, String> {

	Optional<ConfigVariable> findByVarKey(String varKey);
}
