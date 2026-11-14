package ru.itmo.airlines.core.service;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.itmo.airlines.core.dao.entity.RunEntity;
import ru.itmo.airlines.core.dao.repository.RunRepository;
import ru.itmo.airlines.core.dto.CreateRunDto;
import ru.itmo.airlines.core.dto.RunDto;
import ru.itmo.airlines.core.dto.UpdateRunDto;
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
        return runMapper.toDto(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<RunDto> getAll() {
        return runRepository.findAll(Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")))
                .stream().map(runMapper::toDto).toList();
    }

    @Transactional
    public RunDto update(UUID id, UpdateRunDto dto) {
        RunEntity entity = findEntity(id);
        if (dto.name() != null) {
            entity.setName(dto.name());
        }
        if (dto.descriptionPresent()) {
            entity.setDescription(dto.description());
        }
        return runMapper.toDto(entity);
    }

    @Transactional
    public void delete(UUID id) {
        runRepository.delete(findEntity(id));
    }

    private RunEntity findEntity(UUID id) {
        return runRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Прогон", id));
    }
}
