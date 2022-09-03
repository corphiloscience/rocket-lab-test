package com.rocketlab.rocketlabtest.telemetry;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public CollectionModel<EntityModel<Telemetry>> all() {
        List<EntityModel<Telemetry>> telemetries = telemetryRepository.findAll().stream()
                .map(telemetry -> EntityModel.of(telemetry,
                        linkTo(methodOn(TelemetryController.class).byId(telemetry.getId())).withSelfRel(),
                        linkTo(methodOn(TelemetryController.class).all()).withRel("telemetry")))
                .toList();
        return CollectionModel.of(telemetries,
                linkTo(methodOn(TelemetryController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Telemetry> byId(@PathVariable Long id) {
        Telemetry telemetry = telemetryRepository.findById(id)
                .orElseThrow(() -> new TelemetryNotFoundException(id));
        return EntityModel.of(telemetry,
                linkTo(methodOn(TelemetryController.class).byId(id)).withSelfRel(),
                linkTo(methodOn(TelemetryController.class).all()).withRel("telemetry"));
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
