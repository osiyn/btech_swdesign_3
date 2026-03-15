package ru.tigerbank.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.*;

@DisplayName("Тесты Operation")
class OperationTest {
    @Test
    @DisplayName("Создание операции с валидными данными")
    void shouldCreateOperationWithValidData() {
        // Arrange
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Money amount = new Money(new BigDecimal("1500.00"), "RUB");
        LocalDateTime date = LocalDateTime.now();

        // Act
        Operation operation = new Operation(OperationType.EXPENSE, accountId, amount,
                date, categoryId, "Обед в кафе");

        // Assert
        assertNotNull(operation.getId());
        assertEquals(OperationType.EXPENSE, operation.getType());
        assertEquals(accountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(date, operation.getDate());
        assertEquals(categoryId, operation.getCategoryId());
        assertEquals("Обед в кафе", operation.getDescription());
    }
}