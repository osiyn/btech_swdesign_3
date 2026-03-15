package ru.tigerbank.application.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ImportResult {
    boolean success;
    int recordsImported;
    int recordsSkipped;
    List<String> errors;
    List<String> warnings;

    public static ImportResult success(int count) {
        return ImportResult.builder()
                .success(true)
                .recordsImported(count)
                .recordsSkipped(0)
                .errors(List.of())
                .warnings(List.of())
                .build();
    }

    public static ImportResult failure(List<String> errors) {
        return ImportResult.builder()
                .success(false)
                .recordsImported(0)
                .recordsSkipped(0)
                .errors(errors)
                .warnings(List.of())
                .build();
    }
}
