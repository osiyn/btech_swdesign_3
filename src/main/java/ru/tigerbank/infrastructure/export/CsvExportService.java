package ru.tigerbank.infrastructure.export;

import java.io.StringWriter;
import java.util.List;

import com.opencsv.CSVWriter;

import ru.tigerbank.application.interfaces.repository.IOperationRepository;
import ru.tigerbank.domain.model.BankAccount;
import ru.tigerbank.domain.model.Operation;
import ru.tigerbank.application.interfaces.services.IExportService;

public class CsvExportService implements IExportService {
    private final IOperationRepository operationRepository;

    public CsvExportService(IOperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Override
    public String export(List<BankAccount> accounts) {
        StringWriter stringWriter = new StringWriter();
        try (CSVWriter writer = new CSVWriter(stringWriter)) {
            String[] header = {"Дата", "Счет", "Тип", "Категория", "Сумма", "Валюта", "Описание"};
            writer.writeNext(header);

            // Данные по каждому счету
            for (BankAccount account : accounts) {
                List<Operation> operations = operationRepository.findByAccount(account.getId());
                for (Operation op : operations) {
                    String[] line = {
                            op.getDate().toLocalDate().toString(),
                            account.getName(),
                            op.getType().getDisplayName(),
                            op.getCategoryId().toString(),
                            op.getAmount().getAmount().toString(),
                            op.getAmount().getCurrencyCode(),
                            op.getDescription() != null ? op.getDescription() : ""
                    };
                    writer.writeNext(line);
                }
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