package ru.tigerbank.application.command;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import ru.tigerbank.application.interfaces.command.ICommand;

@Component
@RequiredArgsConstructor
public class CommandInvoker {

    public <T> T execute(ICommand<T> command) {
        // Здесь можно добавить: логирование, транзакции, валидацию
        return command.execute();
    }
}
