package com.rocketlab.rocketlabtest.telemetry;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TelemetryNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(TelemetryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String telemetryNotFoundHandler(TelemetryNotFoundException ex) {
        return ex.getMessage();
    }
}
