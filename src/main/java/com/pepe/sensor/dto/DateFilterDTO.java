package com.pepe.sensor.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class DateFilterDTO {

    private Date date;
    private int tz;
    private int minutes;

    public DateFilterDTO(Date date, int tz, int minutes) {
        this.date = date;
        this.tz = tz;
        this.minutes = minutes;
    }

    public Timestamp getBegin() {
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        return new Timestamp(date.getTime() + (tz + minutes) * 60000);
    }

    public Timestamp getEnd() {
        return new Timestamp(getBegin().getTime() + 86400000 - 1);
    }
}
