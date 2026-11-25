package ru.itmo.airlines.core.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.airlines.core.dao.entity.FlightEntity;

import java.util.UUID;

public interface FlightRepository extends JpaRepository<FlightEntity, UUID> {
    boolean existsByRun_Id(UUID runId);
}
