package ru.tigerbank.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImportFormat {
    CSV("csv", "text/csv"),
    JSON("json", "application/json"),
    YAML("yaml", "application/x-yaml");

    private final String extension;
    private final String mimeType;

    public static ImportFormat fromFilename(String filename) {
        if (filename == null) return null;
        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return switch (ext) {
            case "csv" -> CSV;
            case "json" -> JSON;
            case "yaml", "yml" -> YAML;
            default -> null;
        };
    }
}
