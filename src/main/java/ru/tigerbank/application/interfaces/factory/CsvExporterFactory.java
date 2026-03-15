package ru.tigerbank.application.interfaces.factory;

import ru.tigerbank.application.interfaces.services.IExportService;
import ru.tigerbank.domain.enums.ExportFormat;
import ru.tigerbank.infrastructure.export.CsvExportService;

public class CsvExporterFactory extends ExporterFactory{
    @Override
    public IExportService createExporter() {
        return new CsvExportService();
    }

    @Override
    public ExportFormat getSupportedFormat() {
        return ExportFormat.CSV;
    }
}
