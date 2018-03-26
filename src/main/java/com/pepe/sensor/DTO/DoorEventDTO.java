package com.pepe.sensor.DTO;

import com.pepe.sensor.persistence.DoorEvent;
import java.sql.Timestamp;

public class DoorEventDTO {

    long id;
    Timestamp timestamp;
    String token;

    public DoorEventDTO() {
    }

    public DoorEventDTO(DoorEvent doorEvent) {
        this.id = doorEvent.getId();
        this.timestamp = doorEvent.getTimestamp();
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
