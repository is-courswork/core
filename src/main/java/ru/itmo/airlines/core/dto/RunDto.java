package ru.itmo.airlines.core.dto;

import java.time.Instant;
import java.util.UUID;

import ru.itmo.airlines.core.model.RunStatus;

/** Прогон. */
public record RunDto(
        UUID id,
        String name,
        String description,
        RunStatus status,
        Instant createdAt,
        Instant startedAt,
        Instant finishedAt
) {
}
