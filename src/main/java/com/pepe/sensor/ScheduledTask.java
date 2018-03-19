package com.pepe.sensor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ScheduledTask {

    @Autowired
    private RestClient restClient;

    @Scheduled(fixedRate = 7200000, initialDelay = 1000) // Every 2 hours
    public void postTempHumidity() throws Exception {
        restClient.postTempHumidity();
    }

    @Scheduled(fixedRate = 1080000, initialDelay = 1000) // Every 3 hours
    public void postDoorEvent() throws Exception {
        restClient.postDoorEvent();
    }

    @Scheduled(fixedRate = 14400000, initialDelay = 1000) // Every 4 hours
    public void postSensorReading() throws Exception {
        restClient.postSensorReading();
    }
}
