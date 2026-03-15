package ru.tigerbank.infrastructure.di;

import ru.tigerbank.application.interfaces.repository.IBankAccountRepository;
import ru.tigerbank.application.interfaces.repository.ICategoryRepository;
import ru.tigerbank.application.interfaces.repository.IOperationRepository;
import ru.tigerbank.application.interfaces.services.IAnalyticsService;
import ru.tigerbank.application.interfaces.services.IBankAccountService;
import ru.tigerbank.application.interfaces.services.ICategoryService;
import ru.tigerbank.application.interfaces.services.IExportService;
import ru.tigerbank.application.interfaces.services.IOperationService;
import ru.tigerbank.application.services.*;
import ru.tigerbank.infrastructure.db.*;
import ru.tigerbank.infrastructure.export.CsvExportService;
import ru.tigerbank.infrastructure.export.JsonExportService;
import ru.tigerbank.infrastructure.export.YamlExportService;

public class DependencyFactory {
    //Репозитории
    private final IBankAccountRepository bankAccountRepository;
    private final ICategoryRepository categoryRepository;
    private final IOperationRepository operationRepository;

    // Сервисы
    private IBankAccountService bankAccountService;
    private ICategoryService categoryService;
    private IOperationService operationService;
    private IAnalyticsService analyticsService;
    private IExportService jsonExportService;
    private IExportService csvExportService;
    private IExportService yamlExportService;

    public DependencyFactory() {
        this.bankAccountRepository = new LocalBankAccountRepository();
        this.categoryRepository = new LocalCategoryRepository();
        this.operationRepository = new LocalOperationRepository();
    }

    public IBankAccountService getBankAccountService() {
        if (bankAccountService == null) {
            bankAccountService = new BankAccountService(
                    bankAccountRepository,
                    operationRepository
            );
        }
        return bankAccountService;
    }

    public ICategoryService getCategoryService() {
        if (categoryService == null) {
            categoryService = new CategoryService(categoryRepository);
        }
        return categoryService;
    }

    public IOperationService getOperationService() {
        if (operationService == null) {
            operationService = new OperationService(
                    operationRepository,
                    bankAccountRepository,
                    categoryRepository
            );
        }
        return operationService;
    }

    public IAnalyticsService getAnalyticsService() {
        if (analyticsService == null) {
            analyticsService = new AnalyticsService(
                    operationRepository,
                    categoryRepository
            );
        }
        return analyticsService;
    }

    public IExportService getJsonExportService() {
        if (jsonExportService == null) {
            jsonExportService = new JsonExportService();
        }
        return jsonExportService;
    }

    public IExportService getCsvExportService() {
        if (csvExportService == null) {
            csvExportService = new CsvExportService(operationRepository);
        }
        return csvExportService;
    }

    public IExportService getYamlExportService() {
        if (yamlExportService == null) {
            yamlExportService = new YamlExportService();
        }
        return yamlExportService;
    }
}