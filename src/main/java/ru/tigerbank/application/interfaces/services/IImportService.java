package ru.tigerbank.application.interfaces.services;

import ru.tigerbank.application.dto.ImportResult;
import ru.tigerbank.domain.enums.ImportFormat;

import java.io.InputStream;

public interface IImportService {
    ImportResult importData(InputStream inputStream, String targetAccountId);
    ImportFormat getSupportedFormat();
    default boolean validateFile(InputStream inputStream) {
        return true; // по умолчанию всегда валидно
    }
}
