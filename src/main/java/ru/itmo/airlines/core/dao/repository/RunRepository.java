package ru.itmo.airlines.core.dao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.itmo.airlines.core.dao.entity.RunEntity;

public interface RunRepository extends JpaRepository<RunEntity, UUID> {
}
