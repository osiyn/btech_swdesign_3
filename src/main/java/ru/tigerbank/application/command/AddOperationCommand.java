package ru.tigerbank.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.tigerbank.application.interfaces.command.ICommand;
import ru.tigerbank.application.interfaces.services.IOperationService;
import ru.tigerbank.domain.enums.OperationType;
import ru.tigerbank.domain.model.Money;
import ru.tigerbank.domain.model.Operation;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AddOperationCommand implements ICommand<Operation> {
    private final IOperationService operationService;
    private final OperationType type;
    private final UUID bankAccountId;
    private final Money amount;
    private final LocalDateTime date;
    private final UUID categoryId;
    private final String description;

    @Override
    public Operation execute() {
        return operationService.addOperation(
                type, bankAccountId, amount, date, categoryId, description
        );
    }
}
