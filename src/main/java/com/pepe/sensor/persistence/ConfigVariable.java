package com.pepe.sensor.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ConfigVariable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 1, max = 30)
	@Column(nullable = false, length = 30)
	private String varKey;

	@NotNull
	@Size(min = 1, max = 300)
	@Column(nullable = false, length = 300)
	private String varValue;

}
