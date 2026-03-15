package ru.tigerbank.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.tigerbank.application.interfaces.command.ICommand;
import ru.tigerbank.application.interfaces.factory.ExporterFactory;
import ru.tigerbank.application.interfaces.services.IBankAccountService;
import ru.tigerbank.application.interfaces.services.IExportService;
import ru.tigerbank.domain.enums.ExportFormat;
import ru.tigerbank.domain.model.BankAccount;

import java.util.List;

@Getter
@AllArgsConstructor
public class ExportDataCommand implements ICommand<String> {
    private final IBankAccountService accountService;
    private final ExportFormat format;

    @Override
    public String execute() {
        ExporterFactory factory = ExporterFactory.getFactory(format);
        IExportService exporter = factory.createExporter();
        List<BankAccount> accounts = accountService.getAllAccounts();
        return exporter.export(accounts);
    }
}