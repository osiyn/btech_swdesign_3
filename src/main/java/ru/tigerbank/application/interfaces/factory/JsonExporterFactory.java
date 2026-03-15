package ru.tigerbank.application.interfaces.factory;

import ru.tigerbank.application.interfaces.services.IExportService;
import ru.tigerbank.infrastructure.export.JsonExportService;
import ru.tigerbank.domain.enums.ExportFormat;

public class JsonExporterFactory extends ExporterFactory {

    @Override
    public IExportService createExporter() {
        return new JsonExportService();
    }

    @Override
    public ExportFormat getSupportedFormat() {
        return ExportFormat.JSON;
    }
}
