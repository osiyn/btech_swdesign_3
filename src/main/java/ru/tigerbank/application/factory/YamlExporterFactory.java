package ru.tigerbank.application.factory;

import ru.tigerbank.application.interfaces.services.IExportService;
import ru.tigerbank.domain.enums.ExportFormat;
import ru.tigerbank.infrastructure.export.YamlExportService;

public class YamlExporterFactory extends ExporterFactory {
    @Override
    public IExportService createExporter() {
        return new YamlExportService();
    }

    @Override
    public ExportFormat getSupportedFormat() {
        return ExportFormat.YAML;
    }
}
