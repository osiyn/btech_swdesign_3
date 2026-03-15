package ru.tigerbank.application.interfaces.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.Category;

public interface IAnalyticsService {
    BigDecimal getNetIncome(UUID accountId, LocalDate startDate, LocalDate endDate);
    Map<Category, BigDecimal> getCategoryBreakdown(UUID accountId, LocalDate startDate,
                                                   LocalDate endDate, OperationType type);
}