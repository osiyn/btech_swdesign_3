package ru.tigerbank.infrastructure.export;

import java.io.StringWriter;
import java.util.List;

import com.opencsv.CSVWriter;

import ru.tigerbank.application.interfaces.repository.IOperationRepository;
import ru.tigerbank.domain.model.BankAccount;
import ru.tigerbank.domain.model.Operation;
import ru.tigerbank.application.interfaces.services.IExportService;

public class CsvExportService implements IExportService {

    @Override
    public String export(List<BankAccount> accounts) {
        StringWriter stringWriter = new StringWriter();

        try (CSVWriter writer = new CSVWriter(stringWriter)) {
            // Заголовок
            String[] header = {"ID счёта", "Название", "Баланс", "Валюта"};
            writer.writeNext(header);

            // Данные
            for (BankAccount account : accounts) {
                String[] line = {
                        account.getId().toString(),
                        account.getName(),
                        account.getBalance().getAmount().toString(),
                        account.getBalance().getCurrencyCode()
                };
                writer.writeNext(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при экспорте в CSV", e);
        }

        return stringWriter.toString();
    }

    @Override
    public String getFormatName() {
        return "CSV";
    }
}