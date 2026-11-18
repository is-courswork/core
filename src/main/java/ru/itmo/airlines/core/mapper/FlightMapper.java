package ru.itmo.airlines.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itmo.airlines.core.api.model.CreateFlightRequest;
import ru.itmo.airlines.core.api.model.FlightResponse;
import ru.itmo.airlines.core.dao.entity.FlightEntity;
import ru.itmo.airlines.core.dto.CreateFlightDto;
import ru.itmo.airlines.core.dto.FlightDto;

import java.util.UUID;

@Mapper
public interface FlightMapper {

    CreateFlightDto toDto(CreateFlightRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "run", ignore = true)
    FlightEntity toEntity(CreateFlightDto dto);

    @Mapping(target = "runId", source = "entity.run.id")
    @Mapping(target = "airlineAccountId", source = "airlineAccountId")
    FlightDto toDto(FlightEntity entity, UUID airlineAccountId);

    FlightResponse toResponse(FlightDto dto);
}
