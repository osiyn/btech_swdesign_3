package ru.tigerbank.application.interfaces.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.tigerbank.domain.model.Operation;
import ru.tigerbank.domain.enums.OperationType;

public interface IOperationRepository {
    Operation save(Operation operation);
    Optional<Operation> findById(UUID id);
    List<Operation> findAll();
    List<Operation> findByAccount(UUID accountId);
    List<Operation> findByAccountAndDateRange(UUID accountId, LocalDateTime startDate, LocalDateTime endDate);
    List<Operation> findByAccountAndType(UUID accountId, OperationType type);
    boolean deleteById(UUID id);
    void deleteAll();
}