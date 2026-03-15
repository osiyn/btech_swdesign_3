package ru.tigerbank.application.interfaces.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.Money;
import ru.tigerbank.domain.model.Operation;

public interface IOperationService {
    Operation addOperation(OperationType type, UUID bankAccountId, Money amount,
                           LocalDateTime date, UUID categoryId, String description);
    Optional<Operation> findById(UUID id);
    List<Operation> getOperationsByAccount(UUID accountId);
    List<Operation> getOperationsByAccountAndPeriod(UUID accountId, LocalDateTime startDate, LocalDateTime endDate);
    boolean deleteOperation(UUID id);
}