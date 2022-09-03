package com.rocketlab.rocketlabtest;

import com.rocketlab.rocketlabtest.telemetry.Telemetry;
import com.rocketlab.rocketlabtest.telemetry.TelemetryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.rocketlab.rocketlabtest.telemetry.Measurement.OEM_SCRATE_BDYX;

@Configuration
public class LoadDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    public CommandLineRunner initDataBase(TelemetryRepository telemetryRepository) {
        List<Telemetry> telemetryData = new ArrayList<>();
        telemetryData.add(new Telemetry(OEM_SCRATE_BDYX, LocalDateTime.parse("2022-05-13T18:51:10"), BigDecimal.valueOf(-5.0690708335691E-06),"2200"));
        telemetryData.add(new Telemetry(OEM_SCRATE_BDYX, LocalDateTime.parse("2022-05-13T18:51:20"), BigDecimal.valueOf(-4.89050479960237E-06),"2200"));
        telemetryData.add(new Telemetry(OEM_SCRATE_BDYX, LocalDateTime.parse("2022-05-13T18:51:30"), BigDecimal.valueOf(-4.64796773182267E-06),"2200"));
        return args -> {
                LOGGER.info("Preloading some data...");
                telemetryData.forEach(t -> LOGGER.info(telemetryRepository.save(t).toString()));
        };
    }
}
