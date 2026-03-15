package ru.tigerbank.application.interfaces.services;

import java.util.List;

import ru.tigerbank.domain.model.BankAccount;

public interface IExportService {
    String export(List<BankAccount> accounts);
    String getFormatName();
}