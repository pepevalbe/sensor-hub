package com.pepe.sensor.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SensorReading implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(nullable = false)
	private Timestamp timestamp;

	@NotNull
	@Column(nullable = false)
	private Float value1;

	@Column
	private Float value2;

	@Column
	private Float value3;

	@ManyToOne
	private Person owner;

	@PrePersist
	protected void onCreate() {
		timestamp = new Timestamp(System.currentTimeMillis());
	}

}
