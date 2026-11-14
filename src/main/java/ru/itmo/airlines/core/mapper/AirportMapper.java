package ru.itmo.airlines.core.mapper;

import org.springframework.stereotype.Component;

import ru.itmo.airlines.core.api.model.AirportResponse;
import ru.itmo.airlines.core.dao.entity.AirportEntity;

@Component
public class AirportMapper {

    public AirportResponse toResponse(AirportEntity airport) {
        return new AirportResponse(
            airport.getIataCode(),
            airport.getName(),
            airport.getCity(),
            airport.getCountry(),
            airport.getLatitude().doubleValue(),
            airport.getLongitude().doubleValue(),
            airport.getTimezone()
        );
    }
}
