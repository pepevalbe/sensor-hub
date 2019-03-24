package com.pepe.sensor.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
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
