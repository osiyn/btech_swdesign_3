package ru.tigerbank.application.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import ru.tigerbank.application.interfaces.repository.ICategoryRepository;
import ru.tigerbank.application.interfaces.repository.IOperationRepository;
import ru.tigerbank.application.interfaces.services.IAnalyticsService;
import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.*;

public class AnalyticsService implements IAnalyticsService {
    private final IOperationRepository operationRepository;
    private final ICategoryRepository categoryRepository;

    public AnalyticsService(IOperationRepository operationRepository,
                            ICategoryRepository categoryRepository) {
        this.operationRepository = operationRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public BigDecimal getNetIncome(UUID accountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Operation> operations = operationRepository.findByAccountAndDateRange(
                accountId, startDateTime, endDateTime
        );

        BigDecimal income = operations.stream()
                .filter(op -> op.getType() == OperationType.INCOME)
                .map(op -> op.getAmount().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = operations.stream()
                .filter(op -> op.getType() == OperationType.EXPENSE)
                .map(op -> op.getAmount().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return income.subtract(expense);
    }

    @Override
    public Map<Category, BigDecimal> getCategoryBreakdown(UUID accountId,
                                                          LocalDate startDate,
                                                          LocalDate endDate,
                                                          OperationType type) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Operation> operations = operationRepository.findByAccountAndDateRange(
                        accountId, startDateTime, endDateTime
                ).stream()
                .filter(op -> op.getType() == type)
                .collect(Collectors.toList());

        // Группировка по категориям
        Map<UUID, List<Operation>> groupedByCategory = operations.stream()
                .collect(Collectors.groupingBy(Operation::getCategoryId));

        // Преобразование в мапу категория -> сумма
        Map<Category, BigDecimal> result = new HashMap<>();
        for (Map.Entry<UUID, List<Operation>> entry : groupedByCategory.entrySet()) {
            UUID categoryId = entry.getKey();
            List<Operation> ops = entry.getValue();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Категория с ID " + categoryId + " не найдена"
                    ));

            BigDecimal total = ops.stream()
                    .map(op -> op.getAmount().getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            result.put(category, total);
        }

        return result;
    }
}