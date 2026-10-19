package ru.itmo.airlines.core.model;

/** Статус прогона. Общий для DTO и сущностей БД. */
public enum RunStatus {
    DRAFT,
    RUNNING,
    PAUSED,
    FINISHED,
    CANCELLED,
    FAILED
}
