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
    public Telemetry newTelemetry(@RequestBody Telemetry telemetry) {
        return telemetryRepository.save(telemetry);
    }

    @PostMapping("/bulk")
    public List<Telemetry> newBulkTelemetry(@RequestBody List<Telemetry> telemetries) {
        return telemetryRepository.saveAll(telemetries);
    }

    @GetMapping
    public List<Telemetry> all() {
        return telemetryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Telemetry byId(@PathVariable Long id) {
        return telemetryRepository.findById(id).orElseThrow(() -> new TelemetryNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Telemetry replaceTelemetry(@RequestBody Telemetry telemetry, @PathVariable Long id) {
        return telemetryRepository.findById(id)
                .map(tel -> {
                    tel.setMeasurement(telemetry.getMeasurement());
                    tel.setCraftId(telemetry.getCraftId());
                    tel.setPosition(telemetry.getPosition());
                    tel.setTime(telemetry.getTime());
                    return telemetryRepository.save(tel);
                })
                .orElseGet(() -> {
                    telemetry.setId(id);
                    return telemetryRepository.save(telemetry);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteTelemetry(@PathVariable Long id) {
        telemetryRepository.deleteById(id);
    }
}
