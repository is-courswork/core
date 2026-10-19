package ru.itmo.airlines.core.mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.itmo.airlines.core.api.model.CreateRunRequest;
import ru.itmo.airlines.core.api.model.RunResponse;
import ru.itmo.airlines.core.dao.entity.RunEntity;
import ru.itmo.airlines.core.dto.CreateRunDto;
import ru.itmo.airlines.core.dto.RunDto;


@Mapper
public interface RunMapper {

    CreateRunDto toDto(CreateRunRequest request);

    RunDto toDto(RunEntity entity);

    RunResponse toResponse(RunDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    @Mapping(target = "finishedAt", ignore = true)
    RunEntity toEntity(CreateRunDto dto);

    default OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
    }
}
