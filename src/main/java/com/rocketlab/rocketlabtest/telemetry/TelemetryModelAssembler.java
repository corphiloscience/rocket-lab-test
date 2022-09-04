package com.rocketlab.rocketlabtest.telemetry;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TelemetryModelAssembler implements RepresentationModelAssembler<Telemetry, EntityModel<Telemetry>> {
    @Override
    public EntityModel<Telemetry> toModel(Telemetry telemetry) {
        return EntityModel.of(telemetry,
                linkTo(methodOn(TelemetryController.class).byId(telemetry.getId())).withSelfRel(),
                linkTo(methodOn(TelemetryController.class).all()).withRel("telemetry"));
    }

    @Override
    public CollectionModel<EntityModel<Telemetry>> toCollectionModel(Iterable<? extends Telemetry> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
