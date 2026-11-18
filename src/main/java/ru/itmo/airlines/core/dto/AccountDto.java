package ru.itmo.airlines.core.dto;

import ru.itmo.airlines.core.model.AccountOwnerType;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountDto(
    UUID id,
    UUID runId,
    AccountOwnerType ownerType,
    BigDecimal balance
) {
}
