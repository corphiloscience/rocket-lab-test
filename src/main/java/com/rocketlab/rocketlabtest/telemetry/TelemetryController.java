package com.rocketlab.rocketlabtest.telemetry;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/telemetry")
public class TelemetryController {

    private final TelemetryRepository repository;

    private final TelemetryModelAssembler assembler;

    public TelemetryController(TelemetryRepository repository, TelemetryModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Telemetry>> newTelemetry(@RequestBody Telemetry telemetry) {
        EntityModel<Telemetry> entityModel = assembler.toModel(repository.save(telemetry));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PostMapping("/bulk")
    public ResponseEntity<CollectionModel<EntityModel<Telemetry>>> newBulkTelemetry(@RequestBody List<Telemetry> telemetries) {
        List<EntityModel<Telemetry>> telemetryModels = repository.saveAll(telemetries).stream()
                .map(assembler::toModel)
                .toList();
        CollectionModel<EntityModel<Telemetry>> collectionModel = CollectionModel.of(telemetryModels,
                linkTo(methodOn(TelemetryController.class).newBulkTelemetry(telemetries)).withSelfRel());
        return ResponseEntity
                .created(collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(collectionModel);
    }

    @GetMapping
    public CollectionModel<EntityModel<Telemetry>> all() {
        List<EntityModel<Telemetry>> telemetries = repository.findAll().stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(telemetries,
                linkTo(methodOn(TelemetryController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Telemetry> byId(@PathVariable Long id) {
        Telemetry telemetry = repository.findById(id)
                .orElseThrow(() -> new TelemetryNotFoundException(id));
        return assembler.toModel(telemetry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Telemetry>> replaceTelemetry(@RequestBody Telemetry telemetry, @PathVariable Long id) {
        Telemetry updatedTelemetry = repository.findById(id)
                .map(tel -> {
                    tel.setMeasurement(telemetry.getMeasurement());
                    tel.setCraftId(telemetry.getCraftId());
                    tel.setPosition(telemetry.getPosition());
                    tel.setTime(telemetry.getTime());
                    return repository.save(tel);
                })
                .orElseGet(() -> {
                    telemetry.setId(id);
                    return repository.save(telemetry);
                });
        EntityModel<Telemetry> entityModel = assembler.toModel(updatedTelemetry);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTelemetry(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
