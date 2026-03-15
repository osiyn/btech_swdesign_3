package ru.tigerbank.application.interfaces.repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import ru.tigerbank.domain.model.BankAccount;

public interface IBankAccountRepository {
    BankAccount save(BankAccount account);
    Optional<BankAccount> findById(UUID id);
    List<BankAccount> findAll();
    boolean deleteById(UUID id);
    void deleteAll();
}