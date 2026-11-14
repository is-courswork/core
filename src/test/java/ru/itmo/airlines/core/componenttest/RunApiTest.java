package ru.itmo.airlines.core.componenttest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class RunApiTest extends BaseComponentTest {

    @Test
    void runCanBeCreatedListedUpdatedAndDeleted() throws Exception {
        String name = "n".repeat(120);
        String description = "d".repeat(2000);
        String body = mockMvc.perform(post("/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "%s", "description": "%s"}
                                """.formatted(name, description)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(body, "$.id");

        mockMvc.perform(get("/run/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(description));

        mockMvc.perform(get("/run/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem(id)));

        mockMvc.perform(patch("/run/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated\",\"description\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.description").doesNotExist());

        mockMvc.perform(get("/run/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.description").doesNotExist());

        mockMvc.perform(delete("/run/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/run/{id}", id)).andExpect(status().isNotFound());
        mockMvc.perform(delete("/run/{id}", id)).andExpect(status().isNotFound());
    }
}
