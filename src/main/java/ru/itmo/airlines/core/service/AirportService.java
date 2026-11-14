package ru.itmo.airlines.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.airlines.core.dao.entity.AirportEntity;
import ru.itmo.airlines.core.dao.repository.AirportRepository;
import ru.itmo.airlines.core.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {
    private final AirportRepository airportRepository;

    public AirportEntity getAirportByIataCode(String iataCode) {
        return airportRepository.findById(iataCode)
            .orElseThrow(() -> new NotFoundException("Airport not found"));
    }

    public AirportEntity saveAirport(AirportEntity airport) {
        return airportRepository.save(airport);
    }

    public List<AirportEntity> getAirports(String search) {
        if (search == null || search.isBlank()) {
            return airportRepository.findAll();
        }

        String query = search.trim();

        return airportRepository
            .findByNameContainingOrCityContaining(
                query, query
            );
    }
    public void deleteAirport(String iataCode) {
        airportRepository.deleteById(iataCode);
    }
}
