package ru.itmo.airlines.core.unittest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.itmo.airlines.core.dao.entity.RunEntity;
import ru.itmo.airlines.core.dao.repository.RunRepository;
import ru.itmo.airlines.core.dto.CreateRunDto;
import ru.itmo.airlines.core.dto.RunDto;
import ru.itmo.airlines.core.exception.NotFoundException;
import ru.itmo.airlines.core.mapper.RunMapper;
import ru.itmo.airlines.core.model.RunStatus;
import ru.itmo.airlines.core.service.RunService;
import ru.itmo.airlines.core.util.TimeProvider;

@ExtendWith(MockitoExtension.class)
class RunServiceTest {

    private static final Instant NOW = Instant.parse("2026-10-20T10:00:00Z");

    @Mock
    private RunRepository runRepository;

    @Mock
    private TimeProvider timeProvider;

    private RunService runService;

    @BeforeEach
    void setUp() {
        runService = new RunService(runRepository, Mappers.getMapper(RunMapper.class), timeProvider);
    }

    @Test
    void createSavesDraftRunWithCurrentTime() {
        when(timeProvider.now()).thenReturn(NOW);
        when(runRepository.save(any())).thenAnswer(inv -> {
            RunEntity entity = inv.getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        RunDto run = runService.create(new CreateRunDto("Прогон", "Гипотеза"));

        assertThat(run.id()).isNotNull();
        assertThat(run.name()).isEqualTo("Прогон");
        assertThat(run.description()).isEqualTo("Гипотеза");
        assertThat(run.status()).isEqualTo(RunStatus.DRAFT);
        assertThat(run.createdAt()).isEqualTo(NOW);
        assertThat(run.startedAt()).isNull();
    }

    @Test
    void getThrowsNotFoundForUnknownId() {
        UUID id = UUID.randomUUID();
        when(runRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> runService.get(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
