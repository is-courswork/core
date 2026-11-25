package ru.itmo.airlines.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itmo.airlines.core.api.model.AccountResponse;
import ru.itmo.airlines.core.dao.entity.AccountEntity;
import ru.itmo.airlines.core.dto.AccountDto;

@Mapper
public interface AccountMapper {

    @Mapping(target = "runId", source = "run.id")
    AccountDto toDto(AccountEntity entity);

    AccountResponse toResponse(AccountDto dto);
}
