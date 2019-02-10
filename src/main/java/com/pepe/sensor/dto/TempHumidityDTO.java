package com.pepe.sensor.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class TempHumidityDTO {

    private long id;
    private Timestamp timestamp;
    private double temperature;
    private double humidity;
    private String token;
}
