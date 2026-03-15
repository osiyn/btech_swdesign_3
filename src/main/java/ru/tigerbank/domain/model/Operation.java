package ru.tigerbank.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.exceptions.DomainException;

public class Operation{
    private final UUID id;
    private final OperationType type;
    private final UUID bankAccountId;
    private final Money amount;
    private final LocalDateTime date;
    private final UUID categoryId;
    private final String description;

    public Operation(OperationType type, UUID bankAccountId, Money amount,
                     LocalDateTime date, UUID categoryId, String description) {
        if (type == null) {
            throw new DomainException("Тип операции не может быть null");
        }
        if (bankAccountId == null) {
            throw new DomainException("ID счета не может быть null");
        }
        if (amount == null) {
            throw new DomainException("Сумма операции не может быть null");
        }
        if (date == null) {
            throw new DomainException("Дата операции не может быть null");
        }
        if (categoryId == null) {
            throw new DomainException("ID категории не может быть null");
        }

        this.id = UUID.randomUUID();
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.description = description != null ? description.trim() : null;
    }

    public UUID getId() { return id; }
    public OperationType getType() { return type; }
    public UUID getBankAccountId() { return bankAccountId; }
    public Money getAmount() { return amount; }
    public LocalDateTime getDate() { return date; }
    public UUID getCategoryId() { return categoryId; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("Operation[id=%s, type=%s, amount=%s, date=%s, category=%s]",
                id, type, amount, date, categoryId);
    }
}