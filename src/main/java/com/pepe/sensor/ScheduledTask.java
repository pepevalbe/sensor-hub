package com.pepe.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Autowired
    private RestClient restClient;

    @Scheduled(fixedRate = 3600000, initialDelay = 1000) // Every hour
    public void postTempHumidity() throws Exception {
        restClient.postTempHumidity();
    }

    @Scheduled(fixedRate = 7200000, initialDelay = 1000) // Every 2 hours
    public void postDoorEvent() throws Exception {
        restClient.postDoorEvent();
    }

    @Scheduled(fixedRate = 7200000, initialDelay = 1000) // Every 2 hours
    public void postSensorReading() throws Exception {
        restClient.postSensorReading();
    }
}
