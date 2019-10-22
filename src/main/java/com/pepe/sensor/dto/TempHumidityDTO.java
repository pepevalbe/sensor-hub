package com.pepe.sensor.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TempHumidityDTO {

    private long id;
    private Timestamp timestamp;
    private double temperature;
    private double humidity;
    private String token;
}
