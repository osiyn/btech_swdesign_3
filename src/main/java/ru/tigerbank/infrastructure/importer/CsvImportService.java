package ru.tigerbank.infrastructure.importer;

import ru.tigerbank.domain.enums.ImportFormat;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.opencsv.CSVReader;

@Service
public class CsvImportService extends AbstractImportService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ImportFormat getSupportedFormat() {
        return ImportFormat.CSV;
    }

    @Override
    public boolean validateFile(InputStream inputStream) {
        try (var reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] header = reader.readNext();
            if (header == null) return false;

            // Минимальный набор колонок
            Set<String> required = Set.of("date", "amount", "currency", "type");
            Set<String> found = new HashSet<>(Arrays.asList(header).subList(0, Math.min(header.length, 7)));

            return found.containsAll(required);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected List<ParsedRecord> parseRecords(InputStream inputStream) throws Exception {
        List<ParsedRecord> records = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] header = reader.readNext(); // пропускаем заголовок
            Map<String, Integer> columnIndex = mapHeaderIndices(header);

            String[] line;
            while ((line = reader.readNext()) != null) {
                records.add(parseLine(line, columnIndex));
            }
        }
        return records;
    }

    private Map<String, Integer> mapHeaderIndices(String[] header) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            map.put(header[i].trim().toLowerCase(), i);
        }
        return map;
    }

    private ParsedRecord parseLine(String[] line, Map<String, Integer> colIndex) {
        return ParsedRecord.builder()
                .date(parseDate(get(line, colIndex, "date")))
                .accountName(get(line, colIndex, "account"))
                .operationType(get(line, colIndex, "type"))
                .categoryName(get(line, colIndex, "category"))
                .amount(parseAmount(get(line, colIndex, "amount")))
                .currency(get(line, colIndex, "currency"))
                .description(get(line, colIndex, "description"))
                .externalId(get(line, colIndex, "id"))
                .build();
    }

    private String get(String[] line, Map<String, Integer> colIndex, String key) {
        Integer idx = colIndex.get(key);
        return (idx != null && idx < line.length) ? line[idx].trim() : null;
    }

    private LocalDateTime parseDate(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return LocalDateTime.parse(value, DATE_FORMAT);
        } catch (Exception e) {
            // Пробуем альтернативные форматы
            try {
                return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    private BigDecimal parseAmount(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return new BigDecimal(value.replace(",", ".").replaceAll("[^\\d.-]", ""));
        } catch (Exception e) {
            return null;
        }
    }
}
