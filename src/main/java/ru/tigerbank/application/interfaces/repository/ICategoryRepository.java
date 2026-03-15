package ru.tigerbank.application.interfaces.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.tigerbank.domain.model.Category;

public interface ICategoryRepository {
    Category save(Category category);
    Optional<Category> findById(UUID id);
    List<Category> findAll();
    List<Category> findByType(ru.tigerbank.domain.enums.OperationType type);
    boolean deleteById(UUID id);
    void deleteAll();
}