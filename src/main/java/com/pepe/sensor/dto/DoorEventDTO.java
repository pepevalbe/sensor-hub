package com.pepe.sensor.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DoorEventDTO {

	private long id;
	private Timestamp timestamp;
	private String token;
}
