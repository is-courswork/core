package ru.itmo.airlines.core.dao.repository;

import java.util.UUID;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.itmo.airlines.core.dao.entity.RunEntity;

public interface RunRepository extends JpaRepository<RunEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RunEntity r where r.id = :id")
    Optional<RunEntity> findByIdForUpdate(@Param("id") UUID id);
}
