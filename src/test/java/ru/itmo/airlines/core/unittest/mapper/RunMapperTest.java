package ru.itmo.airlines.core.unittest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import ru.itmo.airlines.core.api.model.RunResponse;
import ru.itmo.airlines.core.dto.RunDto;
import ru.itmo.airlines.core.mapper.RunMapper;
import ru.itmo.airlines.core.model.RunStatus;

class RunMapperTest {

    private final RunMapper mapper = Mappers.getMapper(RunMapper.class);

    @Test
    void toResponseConvertsStatusAndTimes() {
        UUID id = UUID.randomUUID();
        Instant created = Instant.parse("2026-10-20T10:00:00Z");
        RunDto dto = new RunDto(id, "Прогон", null, RunStatus.RUNNING, created, created, null);

        RunResponse response = mapper.toResponse(dto);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getStatus()).isEqualTo(ru.itmo.airlines.core.api.model.RunStatus.RUNNING);
        assertThat(response.getCreatedAt()).isEqualTo(OffsetDateTime.parse("2026-10-20T10:00:00Z"));
        assertThat(response.getStartedAt()).isEqualTo(response.getCreatedAt());
        assertThat(response.getFinishedAt()).isNull();
        assertThat(response.getDescription()).isNull();
    }
}
