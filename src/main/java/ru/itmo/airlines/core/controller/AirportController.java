package ru.itmo.airlines.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.airlines.core.api.AirportApi;
import ru.itmo.airlines.core.api.model.AirportResponse;
import ru.itmo.airlines.core.mapper.AirportMapper;
import ru.itmo.airlines.core.service.AirportService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AirportController implements AirportApi {
    private final AirportService airportService;
    private final AirportMapper airportMapper;

    @Override
    public AirportResponse getAirport(String iataCode) {
        return airportMapper.toResponse(airportService.getAirportByIataCode(iataCode));
    }

    @Override
    public List<AirportResponse> listAirports(String search) {
        return airportService.getAirports(search).stream()
            .map(airportMapper::toResponse)
            .toList();
    }
}
