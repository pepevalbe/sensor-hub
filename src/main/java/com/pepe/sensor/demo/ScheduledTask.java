package com.pepe.sensor.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/* Component responsible of periodically populating data to demo user account */
@Component
public class ScheduledTask {

	@Autowired
	private DemoUserSensor demoUserSensor;

	@Scheduled(fixedRate = 3600000, initialDelay = 1000) // Every hour
	public void postTempHumidity() throws JsonProcessingException {
		demoUserSensor.postTempHumidity();
	}

	@Scheduled(fixedRate = 7200000, initialDelay = 1000) // Every 2 hours
	public void postDoorEvent() throws InterruptedException {
		demoUserSensor.postDoorEvent();
	}

	@Scheduled(fixedRate = 7200000, initialDelay = 1000) // Every 2 hours
	public void postSensorReading() {
		demoUserSensor.postSensorReading();
	}
}
