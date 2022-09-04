package com.rocketlab.rocketlabtest.telemetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TelemetryController.class)
public class TelemetryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TelemetryRepository telemetryRepository;

    @MockBean
    private TelemetryModelAssembler mockAssembler;

    private static final Telemetry TELEMETRY_1 = new Telemetry(1L, Measurement.OEM_SCRATE_BDYX, LocalDateTime.now(), BigDecimal.valueOf(0.01), "2200");
    private static final Telemetry TELEMETRY_2 = new Telemetry(2L, Measurement.OEM_SCRATE_BDYX, LocalDateTime.now().plusSeconds(1), BigDecimal.valueOf(0.02), "2200");
    private static final Telemetry TELEMETRY_3 = new Telemetry(3L, Measurement.OEM_SCRATE_BDYX, LocalDateTime.now().plusSeconds(2), BigDecimal.valueOf(0.03), "2200");
    private static final EntityModel<Telemetry> TELEMETRY_MODEL_1 = mockEntityModel(TELEMETRY_1);
    private static final EntityModel<Telemetry> TELEMETRY_MODEL_2 = mockEntityModel(TELEMETRY_2);
    private static final EntityModel<Telemetry> TELEMETRY_MODEL_3 = mockEntityModel(TELEMETRY_3);

    @Test
    void getAllTelemetry_success() throws Exception {
        List<Telemetry> records = List.of(TELEMETRY_1, TELEMETRY_2, TELEMETRY_3);

        when(telemetryRepository.findAll()).thenReturn(records);
        when(mockAssembler.toModel(eq(TELEMETRY_1))).thenReturn(TELEMETRY_MODEL_1);
        when(mockAssembler.toModel(eq(TELEMETRY_2))).thenReturn(TELEMETRY_MODEL_2);
        when(mockAssembler.toModel(eq(TELEMETRY_3))).thenReturn(TELEMETRY_MODEL_3);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/telemetry")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("_embedded.telemetries", hasSize(3)))
                .andExpect(jsonPath("_embedded.telemetries[1].position", is(0.02)))
                .andExpect(jsonPath("_embedded.telemetries[1]._links.self.href", is("/telemetry/2")));
    }

    @Test
    void createTelemetry_success() throws Exception {
        Telemetry telemetry = Telemetry.builder()
                .id(5L)
                .measurement(Measurement.OEM_SCRATE_BDYX)
                .craftId("2200")
                .time(LocalDateTime.now())
                .position(BigDecimal.valueOf(0.05))
                .build();

        EntityModel<Telemetry> telemetryModel = mockEntityModel(telemetry);

        when(telemetryRepository.save(telemetry)).thenReturn(telemetry);
        when(mockAssembler.toModel(eq(telemetry))).thenReturn(telemetryModel);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/telemetry")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(telemetry));

        mockMvc.perform(mockRequest)
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.position", is(0.05)));
    }

    @Test
    void getTelemetryById_success() throws Exception {
        when(telemetryRepository.findById(eq(TELEMETRY_1.getId()))).thenReturn(Optional.of(TELEMETRY_1));
        when(mockAssembler.toModel(eq(TELEMETRY_1))).thenReturn(TELEMETRY_MODEL_1);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/telemetry/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.position", is(0.01)))
                .andExpect(jsonPath("$._links.self.href", is("/telemetry/1")));
    }

    @Test
    void getTelemetryById_notFound() throws Exception {
        when(telemetryRepository.findById(eq(TELEMETRY_1.getId()))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/telemetry/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TelemetryNotFoundException))
                .andExpect(result -> assertEquals("Could not find telemetry for 1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void updateTelemetry_success() throws Exception {
        Telemetry updatedTelemetry = Telemetry.builder()
                .id(3L)
                .measurement(Measurement.OEM_SCRATE_BDYX)
                .craftId("2200")
                .time(LocalDateTime.now())
                .position(BigDecimal.valueOf(0.03))
                .build();

        when(telemetryRepository.findById(eq(TELEMETRY_3.getId()))).thenReturn(Optional.of(TELEMETRY_3));
        when(telemetryRepository.save(eq(updatedTelemetry))).thenReturn(updatedTelemetry);
        when(mockAssembler.toModel(updatedTelemetry)).thenReturn(mockEntityModel(updatedTelemetry));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/telemetry/3")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedTelemetry));

        mockMvc.perform(mockRequest)
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.position", is(0.03)));
    }

    @Test
    public void deletePatientById_success() throws Exception {
        when(telemetryRepository.findById(TELEMETRY_2.getId())).thenReturn(Optional.of(TELEMETRY_2));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/telemetry/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }


    private static EntityModel<Telemetry> mockEntityModel(Telemetry telemetry) {
        return EntityModel.of(telemetry,
                linkTo(methodOn(TelemetryController.class).byId(telemetry.getId())).withSelfRel(),
                linkTo(methodOn(TelemetryController.class).all()).withRel("telemetry"));
    }
}
