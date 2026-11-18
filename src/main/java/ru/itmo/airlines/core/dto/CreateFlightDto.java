package ru.itmo.airlines.core.dto;

import java.util.UUID;

public record CreateFlightDto(
        UUID runId,
        int economySeats,
        int businessSeats
) {
}
