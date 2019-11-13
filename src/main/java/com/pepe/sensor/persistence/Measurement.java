package com.pepe.sensor.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Measurement {

	private String username;
	private long timestamp;
	private MeasurementType type;
	private Float value1;
	private Float value2;
	private Float value3;
}
