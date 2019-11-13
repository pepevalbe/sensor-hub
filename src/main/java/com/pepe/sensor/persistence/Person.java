package com.pepe.sensor.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Person {

	@Id
	private String username;
	private long creationTimestamp;
	private String password;
	private String role;
	@Indexed(unique = true)
	private String email;
	private String firstName;
	private String lastName;
	private String token;
	private boolean activated;
	private boolean doorRegisterActiveFlag;
	private TemporaryToken temporaryToken;

	public Person(String username, String password, String role, String email, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
