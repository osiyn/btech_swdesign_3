package ru.tigerbank.infrastructure.importer;

import ru.tigerbank.application.dto.ImportResult;
import ru.tigerbank.application.interfaces.services.IImportService;
import ru.tigerbank.domain.model.Operation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractImportService implements IImportService {

    //Шаблонный метод
    @Override
    public final ImportResult importData(InputStream inputStream, String targetAccountId) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        try {
            if (!validateFile(inputStream)) {
                errors.add("Файл не прошёл валидацию для формата " + getSupportedFormat());
                return ImportResult.failure(errors);
            }

            List<ParsedRecord> records = parseRecords(inputStream);
            if (records.isEmpty()) {
                warnings.add("Файл пуст или не содержит данных");
                return ImportResult.success(0);
            }

            List<ParsedRecord> validRecords = validateRecords(records, errors, warnings);

            return ImportResult.builder()
                    .success(true)
                    .recordsImported(validRecords.size())
                    .recordsSkipped(records.size() - validRecords.size())
                    .errors(errors)
                    .warnings(warnings)
                    .build();

        } catch (Exception e) {
            errors.add("Ошибка импорта: " + e.getMessage());
            return ImportResult.failure(errors);
        }
    }

    protected abstract List<ParsedRecord> parseRecords(InputStream inputStream) throws Exception;

    @Override
    public boolean validateFile(InputStream inputStream) {
        return true; // по умолчанию — без валидации
    }

    protected List<ParsedRecord> validateRecords(
            List<ParsedRecord> records, List<String> errors, List<String> warnings) {

        List<ParsedRecord> valid = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            ParsedRecord record = records.get(i);
            if (isValidRecord(record, errors)) {
                valid.add(record);
            } else {
                warnings.add("Запись #" + (i + 1) + " пропущена: невалидные данные");
            }
        }
        return valid;
    }

    protected boolean isValidRecord(ParsedRecord record, List<String> errors) {
        if (record.date == null) return false;
        if (record.amount == null || record.amount.compareTo(java.math.BigDecimal.ZERO) <= 0) return false;
        if (record.currency == null || record.currency.isEmpty()) return false;
        return true;
    }

    @lombok.Value
    @lombok.Builder
    public static class ParsedRecord {
        java.time.LocalDateTime date;
        String accountName;
        String operationType;  // "INCOME" или "EXPENSE"
        String categoryName;
        java.math.BigDecimal amount;
        String currency;
        String description;
        String externalId;  // ID из исходной системы
    }
}
