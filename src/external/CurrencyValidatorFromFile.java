package external;

import domain.exceptions.InvalidCurrencyException;
import domain.external.CurrencyValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CurrencyValidatorFromFile implements CurrencyValidator {

    private final Path filePath;

    public CurrencyValidatorFromFile(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void validate(String currency) throws InvalidCurrencyException {
        try {
            Files.lines(filePath)
                    .filter(currency::equals)
                    .findAny()
                    .orElseThrow(() -> new InvalidCurrencyException());

        } catch (IOException e) {
            throw new InvalidCurrencyException();
        }
    }
}
