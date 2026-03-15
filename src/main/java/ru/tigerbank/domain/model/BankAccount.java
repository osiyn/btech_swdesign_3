package ru.tigerbank.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.exceptions.DomainException;

public class BankAccount {
    private final UUID id;
    private String name;
    private Money balance; //для хранения значения денег, будем использовать кастомный тип
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BankAccount(String name, Money initialBalance) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException("Название счета не может быть пустым");
        }
        this.id = UUID.randomUUID();
        this.name = name.trim();
        this.balance = initialBalance != null ? initialBalance : new Money(BigDecimal.ZERO, "RUB");
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void deposit(Money amount) {
        if (amount == null) {
            throw new DomainException("Сумма депозита не может быть null");
        }
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void withdraw(Money amount) {
        if (amount == null) {
            throw new DomainException("Сумма снятия не может быть null");
        }
        if (this.balance.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new DomainException(String.format(
                    "Недостаточно средств на счете '%s'. Текущий баланс: %s, запрошено: %s",
                    this.name, this.balance, amount
            ));
        }
        this.balance = new Money(this.balance.getAmount().subtract(amount.getAmount()),
                balance.getCurrencyCode());
        this.updatedAt = LocalDateTime.now();
    }

    public void recalculateBalance(List<Operation> operations) {
        if (operations == null) {
            throw new DomainException("Список операций не может быть null");
        }

        Money recalculated = operations.stream()
                .filter(op -> op.getBankAccountId().equals(this.id))
                .map(op -> {
                    if (op.getType() == OperationType.INCOME) {
                        return op.getAmount();
                    } else {
                        return op.getAmount().negate();
                    }
                })
                .reduce(new Money(BigDecimal.ZERO, balance.getCurrencyCode()), Money::add);

        this.balance = recalculated;
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public Money getBalance() { return balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return String.format("BankAccount[id=%s, name='%s', balance=%s]",
                id, name, balance);
    }
}