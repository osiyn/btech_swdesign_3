package ru.tigerbank.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.tigerbank.application.interfaces.command.ICommand;
import ru.tigerbank.application.interfaces.services.IBankAccountService;
import ru.tigerbank.domain.model.BankAccount;
import ru.tigerbank.domain.model.Money;

@Getter
@AllArgsConstructor
public class CreateAccountCommand implements ICommand<BankAccount> {
    private final IBankAccountService accountService;
    private final String accountName;
    private final Money initialBalance;

    @Override
    public BankAccount execute() {
        return accountService.createAccount(accountName, initialBalance);
    }
}
