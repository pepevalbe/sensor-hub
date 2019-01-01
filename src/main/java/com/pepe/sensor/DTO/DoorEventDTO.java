package com.pepe.sensor.DTO;

import com.pepe.sensor.persistence.DoorEvent;
import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoorEventDTO {

    private long id;
    private Timestamp timestamp;
    private String token;
    
    public DoorEventDTO(DoorEvent doorEvent) {
        this.id = doorEvent.getId();
        this.timestamp = doorEvent.getTimestamp();
    }
}
