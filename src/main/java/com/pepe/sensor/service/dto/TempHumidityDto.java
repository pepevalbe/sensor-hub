package com.pepe.sensor.service.dto;

import lombok.Data;

@Data
public class TempHumidityDto {

	private long timestamp;
	private double temperature;
	private double humidity;
	private String token;
}
