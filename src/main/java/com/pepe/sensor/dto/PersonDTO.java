package com.pepe.sensor.dto;

import lombok.Data;

@Data
public class PersonDTO {

	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String token;
	private String password;
	private boolean activated;
	private boolean doorRegisterActiveFlag;
}
