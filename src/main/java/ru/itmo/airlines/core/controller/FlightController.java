package ru.itmo.airlines.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.itmo.airlines.core.api.FlightApi;
import ru.itmo.airlines.core.api.model.*;
import ru.itmo.airlines.core.mapper.FlightMapper;
import ru.itmo.airlines.core.service.FlightService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FlightController implements FlightApi {

    private final FlightService flightService;
    private final FlightMapper flightMapper;

    @Override
    public FlightResponse createFlight(CreateFlightRequest request) {
        return flightMapper.toResponse(flightService.createFlight(flightMapper.toDto(request)));
    }

    @Override
    public FlightResponse getFlight(UUID flightId) {
        return flightMapper.toResponse(flightService.getFlight(flightId));
    }

    //эти три попзже
    @Override
    public List<SeatResponse> listFlightSeats(UUID flightId, CabinClass cabinClass) {
        return List.of();
    }

    @Override
    public List<TicketResponse> listFlightTickets(UUID flightId, CabinClass cabinClass, TicketStatus status) {
        return List.of();
    }

    @Override
    public CloseCheckInResponse closeFlightCheckIn(UUID flightId) {
        return null;
    }
}
