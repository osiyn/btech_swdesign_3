package ru.tigerbank.infrastructure.importer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tigerbank.domain.enums.ImportFormat;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class JsonImportService extends AbstractImportService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ImportFormat getSupportedFormat() {
        return ImportFormat.JSON;
    }

    @Override
    protected List<ParsedRecord> parseRecords(InputStream inputStream) throws Exception {
        List<ParsedRecord> records = new ArrayList<>();

        JsonNode root = objectMapper.readTree(inputStream);
        JsonNode operations = root.has("operations") ? root.get("operations") : root;

        if (!operations.isArray()) {
            // Если это одна операция, а не массив
            records.add(parseNode(operations));
        } else {
            for (JsonNode node : operations) {
                records.add(parseNode(node));
            }
        }
        return records;
    }

    private ParsedRecord parseNode(JsonNode node) {
        return ParsedRecord.builder()
                .date(parseDate(node.path("date").asText()))
                .accountName(node.path("account").asText(null))
                .operationType(node.path("type").asText(null))
                .categoryName(node.path("category").asText(null))
                .amount(parseAmount(node.path("amount").asText(null)))
                .currency(node.path("currency").asText("RUB"))
                .description(node.path("description").asText(null))
                .externalId(node.path("id").asText(null))
                .build();
    }

    private LocalDateTime parseDate(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseAmount(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return null;
        }
    }
}