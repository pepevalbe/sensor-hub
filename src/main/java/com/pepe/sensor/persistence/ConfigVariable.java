package com.pepe.sensor.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class ConfigVariable {

	@Id
	private String varKey;
	private String varValue;

}
