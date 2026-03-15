package ru.tigerbank.domain.model;

import java.util.UUID;

import ru.tigerbank.domain.exceptions.DomainException;
import ru.tigerbank.domain.enums.OperationType;

public class Category {
    private final UUID id;
    private final OperationType type;
    private final String name;

    public Category(String name, OperationType type) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException("Название категории не может быть пустым");
        }
        if (type == null) {
            throw new DomainException("Тип категории не может быть null");
        }

        this.id = UUID.randomUUID();
        this.name = name.trim();
        this.type = type;
    }

    public UUID getId() { return id; }
    public OperationType getType() { return type; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return String.format("Category[id=%s, name='%s', type=%s]", id, name, type);
    }
}