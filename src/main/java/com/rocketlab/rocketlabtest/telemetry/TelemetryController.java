package com.rocketlab.rocketlabtest.telemetry;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/telemetry")
public class TelemetryController {

    private final TelemetryRepository telemetryRepository;

    public TelemetryController(TelemetryRepository telemetryRepository) {
        this.telemetryRepository = telemetryRepository;
    }

    @PostMapping
    public Telemetry newTelemetry(@RequestBody Telemetry newTelemetry) {
        return telemetryRepository.save(newTelemetry);
    }

    @GetMapping
    public List<Telemetry> all() {
        return telemetryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Telemetry byId(@PathVariable Long id) {
        return telemetryRepository.findById(id).orElseThrow(() -> new TelemetryNotFoundException(id));
    }
}
