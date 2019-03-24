package com.pepe.sensor.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class SensorReadingDTO {

    private long id;
    private Timestamp timestamp;
    private float value1;
    private float value2;
    private float value3;
    private String token;
}
