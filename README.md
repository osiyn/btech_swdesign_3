## Фабричный метод
```java
public static ExporterFactory getFactory(ExportFormat format) {
        return switch (format) {
            case JSON -> new JsonExporterFactory();
            case CSV -> new CsvExporterFactory();
            case YAML -> new YamlExporterFactory();
        };
    }
```

## Команда
```java
public interface ICommand<T> {
    T execute();
}

public class CommandInvoker {

    public <T> T execute(ICommand<T> command) {
        // Здесь можно добавить: логирование, транзакции, валидацию
        return command.execute();
    }
}
```
и наследники AddOperationCommand, CreateAccountCommand, ExportDataCommand, GetAnalyticsCommand

## Шаблонный метод
```java
protected abstract List<ParsedRecord> parseRecords(InputStream inputStream) throws Exception;
```
