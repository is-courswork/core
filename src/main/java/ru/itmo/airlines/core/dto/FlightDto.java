package ru.itmo.airlines.core.dto;

import java.util.UUID;

public record FlightDto(
        UUID id,
        UUID runId,
        int economySeats,
        int businessSeats,
        UUID airlineAccountId
) {
}
