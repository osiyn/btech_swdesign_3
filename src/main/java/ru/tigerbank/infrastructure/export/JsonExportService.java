package ru.tigerbank.infrastructure.export;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ru.tigerbank.application.interfaces.services.IExportService;
import ru.tigerbank.domain.model.BankAccount;

public class JsonExportService implements IExportService {
    private final ObjectMapper objectMapper;

    public JsonExportService() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public String export(List<BankAccount> accounts) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accounts);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при экспорте в JSON", e);
        }
    }

    @Override
    public String getFormatName() {
        return "JSON";
    }
}