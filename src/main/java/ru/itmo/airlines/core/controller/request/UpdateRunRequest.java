package ru.itmo.airlines.core.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/** Preserves the difference between an omitted description and an explicit null in PATCH. */
@Getter
public class UpdateRunRequest {

    @Size(min = 1, max = 120)
    @Pattern(regexp = "[\\s\\S]*\\S[\\s\\S]*")
    private String name;

    @Size(max = 2000)
    private String description;

    @JsonIgnore
    private boolean descriptionPresent;

    @JsonSetter(nulls = Nulls.FAIL)
    public void setName(String name) {
        this.name = name;
    }

    @JsonSetter
    public void setDescription(String description) {
        this.description = description;
        this.descriptionPresent = true;
    }
}
