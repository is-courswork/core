package ru.itmo.airlines.core.util;

import java.time.Instant;

public interface TimeProvider {

    Instant now();
}
