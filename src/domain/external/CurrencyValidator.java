package domain.external;

import domain.exceptions.InvalidCurrencyException;

public interface CurrencyValidator {
    void validate(String currency)throws InvalidCurrencyException;
}
