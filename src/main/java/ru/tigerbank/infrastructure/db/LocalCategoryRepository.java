package ru.tigerbank.infrastructure.db;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import ru.tigerbank.application.interfaces.repository.ICategoryRepository;
import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.Category;

@Repository
public class LocalCategoryRepository implements ICategoryRepository {
    private final Map<UUID, Category> storage = new HashMap<>();

    @Override
    public Category save(Category category) {
        storage.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Category> findByType(OperationType type) {
        return storage.values().stream()
                .filter(obj -> obj.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(UUID id) {
        return storage.remove(id) != null;
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }
}