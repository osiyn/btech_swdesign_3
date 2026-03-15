package ru.tigerbank.infrastructure.db;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import ru.tigerbank.application.interfaces.repository.IOperationRepository;
import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.Operation;

@Repository
public class LocalOperationRepository implements IOperationRepository {
    private final Map<UUID, Operation> storage = new HashMap<>();

    @Override
    public Operation save(Operation operation) {
        storage.put(operation.getId(), operation);
        return operation;
    }

    @Override
    public Optional<Operation> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Operation> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Operation> findByAccount(UUID accountId) {
        return storage.values().stream()
                .filter(op -> op.getBankAccountId().equals(accountId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByAccountAndDateRange(UUID accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return storage.values().stream()
                .filter(op -> op.getBankAccountId().equals(accountId) &&
                        !op.getDate().isBefore(startDate) &&
                        !op.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByAccountAndType(UUID accountId, OperationType type) {
        return storage.values().stream()
                .filter(op -> op.getBankAccountId().equals(accountId) &&
                        op.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(UUID id) {
        return storage.remove(id) != null;
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }
}