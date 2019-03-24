package com.pepe.sensor.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class DoorEventDTO {

    private long id;
    private Timestamp timestamp;
    private String token;
}
