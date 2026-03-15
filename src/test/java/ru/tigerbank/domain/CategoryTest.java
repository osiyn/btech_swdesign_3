package ru.tigerbank.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.exceptions.DomainException;
import ru.tigerbank.domain.model.*;

@DisplayName("Тесты Category")
class CategoryTest {
    @Test
    @DisplayName("Создание категории с валидными данными")
    void shouldCreateCategoryWithValidData() {
        Category category = new Category("Зарплата", OperationType.INCOME);

        assertNotNull(category.getId());
        assertEquals("Зарплата", category.getName());
        assertEquals(OperationType.INCOME, category.getType());
    }

    @Test
    @DisplayName("Ошибка при создании категории с пустым названием")
    void shouldThrowExceptionOnEmptyName() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new Category("", OperationType.EXPENSE));
        assertTrue(exception.getMessage().contains("Название категории не может быть пустым"));
    }
}