package com.rocketlab.rocketlabtest.telemetry;

public class TelemetryNotFoundException extends RuntimeException {
    public TelemetryNotFoundException(Long id) {
        super("Could not find telemetry for " + id);
    }
}
