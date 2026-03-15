package ru.tigerbank.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.tigerbank.domain.model.BankAccount;
import ru.tigerbank.domain.model.*;

@DisplayName("Тесты BankAccount")
class BankAccountTest {
    @Test
    @DisplayName("Создание счета с валидными данными")
    void shouldCreateAccountWithValidData() {
        BankAccount account = new BankAccount("Основной счет",
                new Money(new BigDecimal("10000.00"), "RUB"));

        assertNotNull(account.getId());
        assertEquals("Основной счет", account.getName());
        assertEquals(new BigDecimal("10000.00"), account.getBalance().getAmount());
        assertNotNull(account.getCreatedAt());
        assertNotNull(account.getUpdatedAt());
    }

    @Test
    @DisplayName("Депозит увеличивает баланс")
    void shouldIncreaseBalanceOnDeposit() {
        BankAccount account = new BankAccount("Счет", new Money(BigDecimal.ZERO, "RUB"));
        Money depositAmount = new Money(new BigDecimal("5000.00"), "RUB");
        BigDecimal initialBalance = account.getBalance().getAmount();

        account.deposit(depositAmount);

        assertEquals(initialBalance.add(depositAmount.getAmount()),
                account.getBalance().getAmount());
        assertTrue(account.getUpdatedAt().isAfter(account.getCreatedAt()));
    }

    @Test
    @DisplayName("Снятие уменьшает баланс")
    void shouldDecreaseBalanceOnWithdrawal() {
        BankAccount account = new BankAccount("Счет", new Money(new BigDecimal("10000.00"), "RUB"));
        Money withdrawalAmount = new Money(new BigDecimal("3000.00"), "RUB");
        BigDecimal initialBalance = account.getBalance().getAmount();

        account.withdraw(withdrawalAmount);

        assertEquals(initialBalance.subtract(withdrawalAmount.getAmount()),
                account.getBalance().getAmount());
    }
}