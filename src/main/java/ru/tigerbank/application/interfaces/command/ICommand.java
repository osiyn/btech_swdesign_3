package ru.tigerbank.application.interfaces.command;

public interface ICommand<T> {
    T execute();
}
