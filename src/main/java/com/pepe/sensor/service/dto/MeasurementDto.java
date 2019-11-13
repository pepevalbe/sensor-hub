package com.pepe.sensor.service.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementDto {
	private long timestamp;
	private Float value1;
	private Float value2;
	private Float value3;
	private String token;

	@JsonAnySetter
	public void handleTempAndHumidity(String key, String value) {
		if ("temperature".equals(key)) {
			this.setValue1(Float.parseFloat(value));
		} else if ("humidity".equals(key)) {
			this.setValue2(Float.parseFloat(value));
		}
	}
}