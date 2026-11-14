package ru.itmo.airlines.core.unittest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.itmo.airlines.core.controller.RunController;
import ru.itmo.airlines.core.controller.advice.ApiExceptionHandler;
import ru.itmo.airlines.core.dto.RunDto;
import ru.itmo.airlines.core.dto.UpdateRunDto;
import ru.itmo.airlines.core.mapper.RunMapper;
import ru.itmo.airlines.core.model.RunStatus;
import ru.itmo.airlines.core.service.RunService;

class RunControllerTest {

    private final RunService service = Mockito.mock(RunService.class);
    private final UUID id = UUID.randomUUID();
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new RunController(service, Mappers.getMapper(RunMapper.class)))
                .setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void patchDistinguishesOmittedDescriptionFromNull() throws Exception {
        when(service.update(eq(id), any())).thenReturn(new RunDto(
                id, "Updated", null, RunStatus.RUNNING, Instant.now(), null, null));

        mvc.perform(patch("/run/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated\"}"))
                .andExpect(status().isOk());
        verify(service).update(id, new UpdateRunDto("Updated", null, false));

        mvc.perform(patch("/run/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":null}"))
                .andExpect(status().isOk());
        verify(service).update(id, new UpdateRunDto(null, null, true));
    }

    @Test
    void invalidRunRequestsAreRejectedBeforeCallingService() throws Exception {
        for (String body : new String[] {
                "{\"name\":\"\"}", "{\"name\":\"   \"}", "{\"name\":null}",
                "{\"name\":\"" + "a".repeat(121) + "\"}",
                "{\"name\":\"Valid\",\"description\":\"" + "a".repeat(2001) + "\"}"}) {
            mvc.perform(patch("/run/{id}", id).contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest());
            mvc.perform(post("/run").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(service);
    }
}
