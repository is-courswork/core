package ru.itmo.airlines.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.airlines.core.dao.entity.AccountEntity;
import ru.itmo.airlines.core.dao.entity.FlightEntity;
import ru.itmo.airlines.core.dao.entity.RunEntity;
import ru.itmo.airlines.core.dao.entity.SeatEntity;
import ru.itmo.airlines.core.dao.repository.AccountRepository;
import ru.itmo.airlines.core.dao.repository.FlightRepository;
import ru.itmo.airlines.core.dao.repository.RunRepository;
import ru.itmo.airlines.core.dao.repository.SeatRepository;
import ru.itmo.airlines.core.dto.CreateFlightDto;
import ru.itmo.airlines.core.dto.FlightDto;
import ru.itmo.airlines.core.exception.BadRequestException;
import ru.itmo.airlines.core.exception.NotFoundException;
import ru.itmo.airlines.core.mapper.FlightMapper;
import ru.itmo.airlines.core.model.AccountOwnerType;
import ru.itmo.airlines.core.model.CabinClass;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final AccountRepository accountRepository;
    private final FlightRepository flightRepository;
    private final RunRepository runRepository;
    private final SeatRepository seatRepository;
    private final FlightMapper flightMapper;

    @Transactional
    public FlightDto createFlight(CreateFlightDto dto) {
        if (dto.economySeats() < 0 || dto.businessSeats() < 0) {
            throw new BadRequestException("Количество мест не может быть отрицательным");
        }

        RunEntity run = runRepository.findByIdForUpdate(dto.runId())
                .orElseThrow(() -> NotFoundException.of("Прогон", dto.runId()));

        if (flightRepository.existsByRun_Id(run.getId())) {
            throw new BadRequestException("У прогона " + run.getId() + " уже есть рейс");
        }

        FlightEntity flight = flightMapper.toEntity(dto);
        flight.setRun(run);
        flight = flightRepository.save(flight);

        AccountEntity account = new AccountEntity();
        account.setRun(run);
        account.setOwnerType(AccountOwnerType.AIRLINE);
        account.setBalance(BigDecimal.ZERO);
        account = accountRepository.save(account);

        createSeats(flight, CabinClass.ECONOMY, dto.economySeats(), "E");
        createSeats(flight, CabinClass.BUSINESS, dto.businessSeats(), "B");

        return flightMapper.toDto(flight, account.getId());
    }

    @Transactional(readOnly = true)
    public FlightDto getFlight(UUID flightId) {
        FlightEntity flightEntity = flightRepository.findById(flightId)
            .orElseThrow(() -> NotFoundException.of("Flight", flightId));
        UUID runId = flightEntity.getRun().getId();
        AccountEntity account = accountRepository.findByRun_IdAndOwnerType(runId, AccountOwnerType.AIRLINE)
            .orElseThrow(() -> new IllegalStateException(
                    "Для рейса " + flightId + " отсутствует счёт авиакомпании"));
        return flightMapper.toDto(flightEntity, account.getId());
    }

    private void createSeats(FlightEntity flight, CabinClass cabinClass, int count, String prefix) {
        for (int i = 0; i < count; i++) {
            SeatEntity seat = new SeatEntity();
            seat.setFlight(flight);
            seat.setCabinClass(cabinClass);
            seat.setSeatNumber(prefix + (i + 1));
            seatRepository.save(seat);
        }
    }
}
