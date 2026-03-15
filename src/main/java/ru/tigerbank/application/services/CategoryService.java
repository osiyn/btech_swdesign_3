package ru.tigerbank.application.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import ru.tigerbank.application.interfaces.repository.ICategoryRepository;
import ru.tigerbank.application.interfaces.services.ICategoryService;
import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.Category;

@Service
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name, OperationType type) {
        Category category = new Category(name, type);
        categoryRepository.save(category);
        return category;
    }

    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByType(OperationType type) {
        return categoryRepository.findByType(type);
    }

    public boolean deleteCategory(UUID id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if(categoryOpt.isEmpty()) {
            return false;
        }
        return categoryRepository.deleteById(id);
    }
}