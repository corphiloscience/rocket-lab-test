package com.rocketlab.rocketlabtest.telemetry;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "telemetry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Measurement measurement;

    private LocalDateTime time;

    @Column(columnDefinition = "NUMERIC(19, 19)")
    private BigDecimal position;

    private String craftId;

    public Telemetry(Measurement measurement, LocalDateTime time, BigDecimal position, String craftId) {
        this.measurement = measurement;
        this.time = time;
        this.position = position;
        this.craftId = craftId;
    }
}
