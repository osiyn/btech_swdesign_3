package ru.tigerbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tigerbank.application.command.CommandInvoker;
import ru.tigerbank.application.command.CreateAccountCommand;
import ru.tigerbank.application.command.ExportDataCommand;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import ru.tigerbank.application.interfaces.services.*;
import ru.tigerbank.domain.enums.ExportFormat;
import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.BankAccount;
import ru.tigerbank.domain.model.Money;
import ru.tigerbank.domain.model.Category;

@SpringBootApplication
public class ConsoleApplication {
    private final IBankAccountService accountService;
    private final ICategoryService categoryService;
    private final IOperationService operationService;
    private final IAnalyticsService analyticsService;
    private final CommandInvoker commandInvoker;

    public ConsoleApplication(IBankAccountService accountService,
                              ICategoryService categoryService,
                              IOperationService operationService,
                              IAnalyticsService analyticsService) {
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.operationService = operationService;
        this.analyticsService = analyticsService;
        this.commandInvoker = new CommandInvoker();
    }

    public void run() {
        System.out.println("=".repeat(70));
        System.out.println("ТИГРБАНК: Модуль «Учет финансов» - Демонстрация");
        System.out.println("=".repeat(70));

        try {
            // 1. Создание счетов
            System.out.println("\nСоздание счетов...");
            BankAccount mainAccount = commandInvoker.execute(new CreateAccountCommand(accountService, "Основной счет",
                    new Money(new BigDecimal("50000.00"), "RUB")));
            System.out.println("Done " + mainAccount);

            BankAccount savingsAccount = commandInvoker.execute(new CreateAccountCommand(accountService, "Сберегательный счет",
                    new Money(new BigDecimal("120000.00"), "RUB")));
            System.out.println("Done " + savingsAccount);

            // 2. Создание категорий
            System.out.println("\n Создание категорий...");
            Category salary = categoryService.createCategory("Зарплата", OperationType.INCOME);
            Category freelance = categoryService.createCategory("Фриланс", OperationType.INCOME);
            Category cafe = categoryService.createCategory("Кафе", OperationType.EXPENSE);
            Category health = categoryService.createCategory("Здоровье", OperationType.EXPENSE);
            Category transport = categoryService.createCategory("Транспорт", OperationType.EXPENSE);

            System.out.println("   Доходы: " + salary.getName() + ", " + freelance.getName());
            System.out.println("   Расходы: " + cafe.getName() + ", " + health.getName() + ", " + transport.getName());

            // 3. Добавление операций
            System.out.println("\n Добавление операций...");
            operationService.addOperation(
                    OperationType.INCOME,
                    mainAccount.getId(),
                    new Money(new BigDecimal("85000.00"), "RUB"),
                    LocalDateTime.now().minusDays(5),
                    salary.getId(),
                    "Ежемесячная зарплата"
            );
            System.out.println("Добавлен доход: Зарплата 85 000 ₽");

            operationService.addOperation(
                    OperationType.EXPENSE,
                    mainAccount.getId(),
                    new Money(new BigDecimal("1250.00"), "RUB"),
                    LocalDateTime.now().minusHours(2),
                    cafe.getId(),
                    "Кофе и десерт в кофейне"
            );
            System.out.println("Добавлен расход: Кафе 1 250 ₽");

            operationService.addOperation(
                    OperationType.EXPENSE,
                    mainAccount.getId(),
                    new Money(new BigDecimal("3500.00"), "RUB"),
                    LocalDateTime.now().minusDays(3),
                    health.getId(),
                    "Посещение стоматолога"
            );
            System.out.println("Добавлен расход: Здоровье 3 500 ₽");

            // Обновление баланса счета после операций
            BankAccount updatedAccount = accountService.findById(mainAccount.getId()).get();
            System.out.println("\nТекущий баланс основного счета: " + updatedAccount.getBalance());

            // 4. Аналитика по категориям
            System.out.println("\nАналитика расходов за последний месяц:");
            Map<Category, BigDecimal> expenseBreakdown = analyticsService.getCategoryBreakdown(
                    mainAccount.getId(),
                    LocalDate.now().minusMonths(1),
                    LocalDate.now(),
                    OperationType.EXPENSE
            );

            expenseBreakdown.forEach((category, amount) ->
                    System.out.printf("%s: %.2f ₽%n", category.getName(), amount)
            );

            BigDecimal netIncome = analyticsService.getNetIncome(
                    mainAccount.getId(),
                    LocalDate.now().minusMonths(1),
                    LocalDate.now()
            );
            System.out.printf("Чистый доход за период: %.2f ₽%n", netIncome);

            // 5. Экспорт данных
            var csvExportCommand = new ExportDataCommand(accountService, ExportFormat.CSV);
            var jsonExportCommand = new ExportDataCommand(accountService, ExportFormat.JSON);
            var yamlExportCommand = new ExportDataCommand(accountService, ExportFormat.YAML);

            System.out.println("\nЭкспорт данных...");
            List<BankAccount> allAccounts = accountService.getAllAccounts();

            // JSON экспорт
            String jsonData = commandInvoker.execute(jsonExportCommand);
            writeToFile("export-finance.json", jsonData);
            System.out.println("Данные экспортированы в export-finance.json");

            // CSV экспорт
//            String csvData = csvExportService.export(allAccounts);
            String csvData = commandInvoker.execute(csvExportCommand);
            writeToFile("export-finance.csv", csvData);
            System.out.println("Данные экспортированы в export-finance.csv");

            // YAML экспорт
//            String yamlData = yamlExportService.export(allAccounts);
            String yamlData = commandInvoker.execute(yamlExportCommand);
            writeToFile("export-finance.yaml", yamlData);
            System.out.println("Данные экспортированы в export-finance.yaml");

            // 6. Пересчет баланса (имитация ошибки)
            System.out.println("\nПересчет баланса после имитации ошибки...");
            // Имитация ошибки: вручную изменяем баланс счета
            BankAccount accountBeforeRecalc = accountService.findById(mainAccount.getId()).get();
            System.out.printf("   Баланс ДО пересчета: %s%n", accountBeforeRecalc.getBalance());

            accountService.recalculateBalance(mainAccount.getId());

            BankAccount accountAfterRecalc = accountService.findById(mainAccount.getId()).get();
            System.out.printf("   Баланс ПОСЛЕ пересчета: %s%n", accountAfterRecalc.getBalance());

        } catch (Exception e) {
            System.err.println("\nОшибка выполнения: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("Демонстрация завершена! Приложение ТигрБанка готово к использованию");
        System.out.println("=".repeat(70));
    }

    private void writeToFile(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи файла " + filename, e);
        }
    }

    public static void main(String[] args) {
//        ConsoleApplication app = new ConsoleApplication();
//        app.run();
        var context = SpringApplication.run(ConsoleApplication.class, args);
        var app = context.getBean(ConsoleApplication.class);
        app.run();
    }
}