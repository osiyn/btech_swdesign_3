package ru.tigerbank.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExportFormat {
    JSON("json", "application/json"),
    CSV("csv", "text/csv"),
    YAML("yaml", "application/x-yaml");

    private final String extension;
    private final String mimeType;

    public String getFilename(String baseName) {
        return baseName + "." + extension;
    }
}