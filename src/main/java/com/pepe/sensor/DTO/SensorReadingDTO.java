package com.pepe.sensor.DTO;

import com.pepe.sensor.persistence.SensorReading;
import java.sql.Timestamp;

public class SensorReadingDTO {

    private long id;
    private Timestamp timestamp;
    private float value1;
    private float value2;
    private float value3;
    private String token;

    public SensorReadingDTO() {
    }

    public SensorReadingDTO(SensorReading sensorReading) {
        this.id = sensorReading.getId();
        this.timestamp = sensorReading.getTimestamp();
        this.value1 = sensorReading.getValue1();
        if (sensorReading.getValue2() != null) {
            this.value2 = sensorReading.getValue2();
        }
        if (sensorReading.getValue3() != null) {
            this.value3 = sensorReading.getValue3();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public float getValue1() {
        return value1;
    }

    public void setValue1(float value1) {
        this.value1 = value1;
    }

    public float getValue2() {
        return value2;
    }

    public void setValue2(float value2) {
        this.value2 = value2;
    }

    public float getValue3() {
        return value3;
    }

    public void setValue3(float value3) {
        this.value3 = value3;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
