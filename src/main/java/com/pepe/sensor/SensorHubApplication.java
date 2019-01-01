package com.pepe.sensor;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableSwagger2 // visit: http://domain/swagger-ui.html#/
@EnableJpaAuditing
public class SensorHubApplication {

    @PostConstruct
    public void init() {
        // Setting Application Time Zone to UTC
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(SensorHubApplication.class, args);
    }
}
