package ru.itmo.airlines.core.dto;

/** Данные для создания прогона. */
public record CreateRunDto(
        String name,
        String description
) {
}
