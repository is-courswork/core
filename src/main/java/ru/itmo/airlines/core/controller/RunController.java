package ru.itmo.airlines.core.controller;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import ru.itmo.airlines.core.api.RunApi;
import ru.itmo.airlines.core.api.model.CreateRunRequest;
import ru.itmo.airlines.core.api.model.RunResponse;
import ru.itmo.airlines.core.controller.request.UpdateRunRequest;
import ru.itmo.airlines.core.dto.UpdateRunDto;
import ru.itmo.airlines.core.mapper.RunMapper;
import ru.itmo.airlines.core.service.RunService;

/** Реализация сгенерированного из контракта интерфейса {@link RunApi}. */
@RestController
@RequiredArgsConstructor
public class RunController implements RunApi {

    private final RunService runService;
    private final RunMapper runMapper;

    @Override
    public RunResponse createRun(CreateRunRequest request) {
        return runMapper.toResponse(runService.create(runMapper.toDto(request)));
    }

    @Override
    public RunResponse getRun(UUID runId) {
        return runMapper.toResponse(runService.get(runId));
    }

    @Override
    public List<RunResponse> getAllRuns() {
        return runService.getAll().stream().map(runMapper::toResponse).toList();
    }

    @Override
    public RunResponse updateRun(UUID runId, UpdateRunRequest request) {
        return runMapper.toResponse(runService.update(runId, new UpdateRunDto(
                request.getName(), request.getDescription(), request.isDescriptionPresent())));
    }

    @Override
    public void deleteRun(UUID runId) {
        runService.delete(runId);
    }
}
