package ru.tigerbank.application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.tigerbank.application.interfaces.repository.IBankAccountRepository;
import ru.tigerbank.application.interfaces.repository.ICategoryRepository;
import ru.tigerbank.application.interfaces.repository.IOperationRepository;
import ru.tigerbank.application.interfaces.services.IOperationService;
import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.exceptions.DomainException;
import ru.tigerbank.domain.model.*;

public class OperationService implements IOperationService {
    private final IOperationRepository operationRepository;
    private final IBankAccountRepository bankAccountRepository;
    private final ICategoryRepository categoryRepository;

    public OperationService(IOperationRepository operationRepository,
                            IBankAccountRepository bankAccountRepository,
                            ICategoryRepository categoryRepository) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Operation addOperation(OperationType type, UUID bankAccountId, Money amount,
                                  LocalDateTime date, UUID categoryId, String description) {

        // Валидация существования счета
        BankAccount account = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new DomainException("Счет с ID " + bankAccountId + " не найден"));

        // Валидация существования категории
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DomainException("Категория с ID " + categoryId + " не найдена"));

        // Валидация соответствия типа операции и категории
        if (category.getType() != type) {
            throw new DomainException(String.format(
                    "Несоответствие типа операции и категории: операция='%s', категория='%s'",
                    type.getDisplayName(), category.getType().getDisplayName()
            ));
        }

        // Создание операции
        Operation operation = new Operation(type, bankAccountId, amount, date, categoryId, description);
        operationRepository.save(operation);

        // Обновление баланса счета
        if (type == OperationType.INCOME) {
            account.deposit(amount);
        } else {
            account.withdraw(amount);
        }
        bankAccountRepository.save(account);

        return operation;
    }

    @Override
    public Optional<Operation> findById(UUID id) {
        return operationRepository.findById(id);
    }

    @Override
    public List<Operation> getOperationsByAccount(UUID accountId) {
        return operationRepository.findByAccount(accountId);
    }

    @Override
    public List<Operation> getOperationsByAccountAndPeriod(UUID accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return operationRepository.findByAccountAndDateRange(accountId, startDate, endDate);
    }

    @Override
    public boolean deleteOperation(UUID id) {
        Optional<Operation> operationOpt = operationRepository.findById(id);
        if (operationOpt.isEmpty()) {
            return false;
        }

        Operation operation = operationOpt.get();

        // Откат операции из баланса счета
        Optional<BankAccount> accountOpt = bankAccountRepository.findById(operation.getBankAccountId());
        if (accountOpt.isPresent()) {
            BankAccount account = accountOpt.get();
            if (operation.getType() == OperationType.INCOME) {
                account.withdraw(operation.getAmount());
            } else {
                account.deposit(operation.getAmount());
            }
            bankAccountRepository.save(account);
        }

        return operationRepository.deleteById(id);
    }
}