package com.pepe.sensor.DTO;

import com.pepe.sensor.persistence.TempHumidity;
import java.sql.Timestamp;

public class TempHumidityDTO {

    long id;
    Timestamp timestamp;
    float temperature;
    float humidity;
    String token;

    public TempHumidityDTO() {
    }

    public TempHumidityDTO(TempHumidity tempHumidity) {
        this.id = tempHumidity.getId();
        this.timestamp = tempHumidity.getTimestamp();
        this.temperature = tempHumidity.getTemperature();
        this.humidity = tempHumidity.getHumidity();
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

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
