package ru.itmo.airlines.core.dao.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.airlines.core.dao.entity.AccountEntity;
import ru.itmo.airlines.core.model.AccountOwnerType;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    public Optional<AccountEntity> findByRun_IdAndOwnerType(UUID run_id, AccountOwnerType ownerType);
}
