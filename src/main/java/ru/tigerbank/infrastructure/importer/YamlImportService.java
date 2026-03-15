package ru.tigerbank.infrastructure.importer;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.LoaderOptions;
import ru.tigerbank.domain.enums.ImportFormat;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class YamlImportService extends AbstractImportService {
    private final LoaderOptions loaderOptions;
    private final Yaml yaml;
    public YamlImportService() {
        this.loaderOptions = new LoaderOptions();
        loaderOptions.setAllowRecursiveKeys(false);  // Запрещаем рекурсивные ключи
        this.yaml = new Yaml(new SafeConstructor(loaderOptions));
    }

    @Override
    public ImportFormat getSupportedFormat() {
        return ImportFormat.YAML;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<ParsedRecord> parseRecords(InputStream inputStream) throws Exception {
        List<ParsedRecord> records = new ArrayList<>();

        Object data = yaml.load(new InputStreamReader(inputStream));

        if (data instanceof Map) {
            Map<String, Object> root = (Map<String, Object>) data;
            Object operations = root.get("operations");

            if (operations instanceof List) {
                for (Object item : (List<?>) operations) {
                    if (item instanceof Map) {
                        records.add(parseMap((Map<String, Object>) item));
                    }
                }
            } else if (operations instanceof Map) {
                records.add(parseMap((Map<String, Object>) operations));
            }
        } else if (data instanceof List) {
            for (Object item : (List<?>) data) {
                if (item instanceof Map) {
                    records.add(parseMap((Map<String, Object>) item));
                }
            }
        }
        return records;
    }

    private ParsedRecord parseMap(Map<String, Object> map) {
        return ParsedRecord.builder()
                .date(parseDate((String) map.get("date")))
                .accountName((String) map.get("account"))
                .operationType((String) map.get("type"))
                .categoryName((String) map.get("category"))
                .amount(parseAmount((String) map.get("amount")))
                .currency((String) map.getOrDefault("currency", "RUB"))
                .description((String) map.get("description"))
                .externalId((String) map.get("id"))
                .build();
    }

    private LocalDateTime parseDate(String value) {
        if (value == null) return null;
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseAmount(String value) {
        if (value == null) return null;
        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            return null;
        }
    }
}