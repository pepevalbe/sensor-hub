package com.pepe.sensor;

import com.pepe.sensor.repository.ConfigVariableRepository;
import java.io.IOException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ScheduledTask {

    @Autowired
    private RestClient restClient;

    @Autowired
    ConfigVariableRepository configVariableRepository;

    // This keeps heroku server awake
    @Scheduled(fixedRate = 3000000) // Every 50 minutes
    public void keepAwake() {
        try {
            URL obj = new URL(configVariableRepository.getValueByKey("APP_BASE_URL")); // get application url
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");    // optional default is GET
            int responseCode = con.getResponseCode();   // make requeset
        } catch (IOException ex) {
            Logger.getLogger(ScheduledTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
