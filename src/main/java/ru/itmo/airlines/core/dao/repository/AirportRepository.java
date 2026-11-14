package ru.itmo.airlines.core.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.itmo.airlines.core.dao.entity.AirportEntity;

import java.util.List;

public interface AirportRepository extends JpaRepository<AirportEntity, String> {
    List<AirportEntity>
    findByNameContainingOrCityContaining(
        String name,
        String city
    );
}
