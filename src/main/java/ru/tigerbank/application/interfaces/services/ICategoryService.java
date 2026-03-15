package ru.tigerbank.application.interfaces.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.Category;

public interface ICategoryService {
    Category createCategory(String name, OperationType type);
    Optional<Category> findById(UUID id);
    List<Category> getAllCategories();
    List<Category> getCategoriesByType(OperationType type);
    boolean deleteCategory(UUID id);
}