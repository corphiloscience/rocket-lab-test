package com.rocketlab.rocketlabtest.telemetry;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "telemetry")
@Data
@NoArgsConstructor
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Measurement measurement;

    private LocalDateTime time;

    private BigDecimal position;

    private String appId;

    public Telemetry(Measurement measurement, LocalDateTime time, BigDecimal position, String appId) {
        this.measurement = measurement;
        this.time = time;
        this.position = position;
        this.appId = appId;
    }
}
