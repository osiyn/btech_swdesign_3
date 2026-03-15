package ru.tigerbank.infrastructure.export.factory;

import ru.tigerbank.application.interfaces.services.IExportService;

public abstract class ExportServiceFactory {
    public abstract IExportService createExportService();
    public abstract String getFormat();

    protected abstract IExportService createConcreteExportService();
}
