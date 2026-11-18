package ru.itmo.airlines.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.airlines.core.api.AccountApi;
import ru.itmo.airlines.core.api.model.AccountResponse;
import ru.itmo.airlines.core.mapper.AccountMapper;
import ru.itmo.airlines.core.service.AccountService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponse getAccount(UUID accountId) {
        return accountMapper.toResponse(accountService.getAccount(accountId));
    }
}
