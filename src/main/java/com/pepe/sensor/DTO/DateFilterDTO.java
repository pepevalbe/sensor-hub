package com.pepe.sensor.DTO;

import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class DateFilterDTO {

    private Date date;
    private int tz;
    private int minutes;
    
    public Timestamp getBegin(){
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        return new Timestamp(date.getTime() + (tz + minutes) * 60000);
    }
    
    public Timestamp getEnd(){
        return new Timestamp(getBegin().getTime() + 86400000 - 1);
    }
}