package ru.tigerbank.domain.model;

import java.util.Currency;
import java.util.Objects;
import java.math.BigDecimal;
import java.math.RoundingMode;

import ru.tigerbank.domain.exceptions.DomainException;

public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, String currencyCode) {
        if (amount == null) {
            throw new DomainException("Сумма не может быть null");
        }
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            throw new DomainException("Код валюты не может быть пустым");
        }

        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        if (this.amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Сумма не может быть отрицательной: " + amount);
        }
        this.currency = Currency.getInstance(currencyCode.toUpperCase());
    }

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new DomainException("Сумма не может быть null");
        }
        if (currency == null) {
            throw new DomainException("Валюта не может быть null");
        }

        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        if (this.amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Сумма не может быть отрицательной: " + amount);
        }
        this.currency = currency;
    }

    public Money add(Money other) {
        validateCurrency(other);
        return new Money(this.amount.add(other.amount), currency);
    }

    public Money substract(Money other) {
        validateCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if(result.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Результат вычитания не может быть отрицательным");
        }
        return new Money(result, currency);
    }

    public Money negate() {
        return new Money(this.amount.negate(), currency);
    }

    private void validateCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new DomainException(String.format(
                    "Невозможно выполнить операцию: %s != %s",
                    this.currency.getCurrencyCode(),
                    other.currency.getCurrencyCode()
            ));
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getCurrencyCode() {
        return currency.getCurrencyCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) &&
                Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency.getCurrencyCode());
    }
}