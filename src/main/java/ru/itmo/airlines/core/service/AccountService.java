package ru.itmo.airlines.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.airlines.core.dao.entity.AccountEntity;
import ru.itmo.airlines.core.dao.repository.AccountRepository;
import ru.itmo.airlines.core.dto.AccountDto;
import ru.itmo.airlines.core.exception.NotFoundException;
import ru.itmo.airlines.core.mapper.AccountMapper;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public AccountDto getAccount(UUID id) {
        return accountMapper.toDto(accountRepository.findById(id).orElseThrow(() -> NotFoundException.of("Account", id)));
    }
}
