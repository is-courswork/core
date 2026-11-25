package ru.itmo.airlines.core.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.airlines.core.dao.entity.SeatEntity;

import java.util.UUID;

public interface SeatRepository extends JpaRepository<SeatEntity, UUID> {
}
