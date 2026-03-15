package ru.tigerbank.application.factory;

import ru.tigerbank.application.interfaces.services.IExportService;
import ru.tigerbank.domain.enums.ExportFormat;

public abstract class ExporterFactory {
    public abstract IExportService createExporter();
    public abstract ExportFormat getSupportedFormat();
    public static ExporterFactory getFactory(ExportFormat format) {
        return switch (format) {
            case JSON -> new JsonExporterFactory();
            case CSV -> new CsvExporterFactory();
            case YAML -> new YamlExporterFactory();
        };
    }
}
