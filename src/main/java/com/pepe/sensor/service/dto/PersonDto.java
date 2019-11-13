package com.pepe.sensor.service.dto;

import lombok.Data;

@Data
public class PersonDto {

	private String username;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String token;
	private boolean activated;
	private boolean doorRegisterActiveFlag;
}
