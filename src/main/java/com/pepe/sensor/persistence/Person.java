package com.pepe.sensor.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Size(min = 3, max = 20)
	@Column(length = 20)
	private String username;

	@NotNull
	@Column(nullable = false)
	private Timestamp creationTimestamp;

	@NotNull
	@Size(min = 3, max = 60)
	@Column(nullable = false, length = 60)
	private String password;

	@NotNull
	@Size(min = 1, max = 10)
	@Column(nullable = false, length = 10)
	private String role;

	@NotNull
	@Size(min = 3, max = 100)
	@Column(unique = true, nullable = false, length = 100)
	private String email;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(nullable = false, length = 100)
	private String firstName;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(nullable = false, length = 100)
	private String lastName;

	@NotNull
	@Size(min = 36, max = 36)
	@Column(nullable = false, length = 36)
	private String token;

	@NotNull
	@Column(nullable = false)
	private boolean activated;

	@NotNull
	@Column(nullable = false)
	private boolean doorRegisterActiveFlag;

	@OneToOne(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private TemporaryToken temporaryToken;

	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DoorEvent> doorEvent;

	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TempHumidity> tempHumidity;

	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SensorReading> sensorReading;

	public Person(String username, String password, String role, String email, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@PrePersist
	protected void onCreate() {
		creationTimestamp = new Timestamp(System.currentTimeMillis());
		token = UUID.randomUUID().toString();
		activated = false;
		doorRegisterActiveFlag = true;
	}
}
