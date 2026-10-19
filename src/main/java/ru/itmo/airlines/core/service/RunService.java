package ru.itmo.airlines.core.service;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.itmo.airlines.core.dao.entity.RunEntity;
import ru.itmo.airlines.core.dao.repository.RunRepository;
import ru.itmo.airlines.core.dto.CreateRunDto;
import ru.itmo.airlines.core.dto.RunDto;
import ru.itmo.airlines.core.exception.NotFoundException;
import ru.itmo.airlines.core.mapper.RunMapper;
import ru.itmo.airlines.core.model.RunStatus;
import ru.itmo.airlines.core.util.TimeProvider;

@Service
@RequiredArgsConstructor
public class RunService {

    private final RunRepository runRepository;
    private final RunMapper runMapper;
    private final TimeProvider timeProvider;

    @Transactional
    public RunDto create(CreateRunDto dto) {
        RunEntity entity = runMapper.toEntity(dto);
        entity.setStatus(RunStatus.DRAFT);
        entity.setCreatedAt(timeProvider.now());
        return runMapper.toDto(runRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public RunDto get(UUID id) {
        return runRepository.findById(id)
                .map(runMapper::toDto)
                .orElseThrow(() -> NotFoundException.of("Прогон", id));
    }
}
