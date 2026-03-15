package ru.tigerbank.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.tigerbank.application.interfaces.command.ICommand;
import ru.tigerbank.application.interfaces.services.IAnalyticsService;
import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetAnalyticsCommand implements ICommand<GetAnalyticsCommand.AnalyticsResult> {
    private final IAnalyticsService analyticsService;
    private final UUID accountId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final OperationType operationType;

    @Override
    public AnalyticsResult execute() {
        Map<Category, BigDecimal> breakdown = analyticsService.getCategoryBreakdown(
                accountId, startDate, endDate, operationType
        );
        BigDecimal netIncome = analyticsService.getNetIncome(accountId, startDate, endDate);
        return new AnalyticsResult(breakdown, netIncome);
    }

    // для результата
    @Getter
    @AllArgsConstructor
    public static class AnalyticsResult {
        private final Map<Category, BigDecimal> categoryBreakdown;
        private final BigDecimal netIncome;
    }
}