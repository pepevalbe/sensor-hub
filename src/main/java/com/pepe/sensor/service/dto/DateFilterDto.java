package com.pepe.sensor.service.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class DateFilterDto {

	private Date date;
	private int tz;
	private int minutes;

	public DateFilterDto(Date date, int tz, int minutes) {
		this.date = date;
		this.tz = tz;
		this.minutes = minutes;
	}

	public long getBegin() {
		if (date == null) {
			date = new Date(System.currentTimeMillis());
		}
		return date.getTime() + (tz + minutes) * 60000;
	}

	public long getEnd() {
		return getBegin() + 86400000 - 1;
	}
}
