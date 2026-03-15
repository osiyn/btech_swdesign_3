package ru.tigerbank.application.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.tigerbank.application.interfaces.repository.IBankAccountRepository;
import ru.tigerbank.application.interfaces.repository.IOperationRepository;
import ru.tigerbank.application.interfaces.services.IBankAccountService;
import ru.tigerbank.domain.model.BankAccount;
import ru.tigerbank.domain.model.Money;
import ru.tigerbank.domain.model.Operation;

public class BankAccountService implements IBankAccountService {
    private final IBankAccountRepository accountRepository;
    private final IOperationRepository operationRepository;

    public BankAccountService(IBankAccountRepository accountRepository,
                              IOperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @Override
    public BankAccount createAccount(String name, Money initialBalance) {
        BankAccount account = new BankAccount(name, initialBalance);
        return accountRepository.save(account);
    }

    @Override
    public Optional<BankAccount> findById(UUID id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<BankAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public boolean deleteAccount(UUID id) {
        // Сначала удаляем все операции по счету для поддержания целостности
        operationRepository.findByAccount(id).forEach(op ->
                operationRepository.deleteById(op.getId())
        );
        return accountRepository.deleteById(id);
    }

    @Override
    public void recalculateBalance(UUID accountId) {
        Optional<BankAccount> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Счет с ID " + accountId + " не найден");
        }

        BankAccount account = accountOpt.get();
        List<Operation> operations = operationRepository.findByAccount(accountId);
        account.recalculateBalance(operations);
        accountRepository.save(account);
    }

    @Override
    public void recalculateAllBalances() {
        List<BankAccount> accounts = accountRepository.findAll();
        for (BankAccount account : accounts) {
            recalculateBalance(account.getId());
        }
    }
}