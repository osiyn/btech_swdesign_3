package ru.tigerbank.application.interfaces.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.tigerbank.domain.model.BankAccount;
import ru.tigerbank.domain.model.Money;

public interface IBankAccountService {
    BankAccount createAccount(String name, Money initialBalance);
    Optional<BankAccount> findById(UUID id);
    List<BankAccount> getAllAccounts();
    boolean deleteAccount(UUID id);
    void recalculateBalance(UUID accountId);
    void recalculateAllBalances();
}