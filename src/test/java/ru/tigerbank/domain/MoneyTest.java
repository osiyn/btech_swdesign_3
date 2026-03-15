package ru.tigerbank.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.tigerbank.domain.model.Money;

@DisplayName("Тесты Object Money")
class MoneyTest {
    @Test
    @DisplayName("Создание корректной суммы")
    void shouldCreateValidMoney() {
        Money money = new Money(new BigDecimal("1000.50"), "RUB");

        assertEquals(new BigDecimal("1000.50"), money.getAmount());
        assertEquals("RUB", money.getCurrencyCode());
        assertEquals("1000.50 RUB", money.toString());
    }

    @Test
    @DisplayName("Сложение корректных сумм")
    void shouldAddValidAmounts() {
        Money money1 = new Money(new BigDecimal("100.00"), "RUB");
        Money money2 = new Money(new BigDecimal("50.50"), "RUB");

        Money result = money1.add(money2);

        assertEquals(new BigDecimal("150.50"), result.getAmount());
    }

    @Test
    @DisplayName("Вычитание корректных сумм")
    void shouldSubtractValidAmounts() {
        Money money1 = new Money(new BigDecimal("100.00"), "RUB");
        Money money2 = new Money(new BigDecimal("30.75"), "RUB");

        Money result = money1.substract(money2);

        assertEquals(new BigDecimal("69.25"), result.getAmount());
    }
}