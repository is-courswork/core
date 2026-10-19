package ru.itmo.airlines.core.componenttest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class RunApiTest extends BaseComponentTest {

    @Test
    void createdRunCanBeFetched() throws Exception {
        String body = mockMvc.perform(post("/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Повышение цены бизнеса", "description": "Гипотеза"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(body, "$.id");

        mockMvc.perform(get("/run/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Повышение цены бизнеса"))
                .andExpect(jsonPath("$.description").value("Гипотеза"));
    }

    @Test
    void emptyNameIsRejected() throws Exception {
        mockMvc.perform(post("/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": ""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
    }

    @Test
    void unknownRunReturns404() throws Exception {
        mockMvc.perform(get("/run/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }
}
