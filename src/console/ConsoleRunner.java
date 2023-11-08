package console;

import domain.external.CurrencyValidator;
import domain.external.ExchangeService;
import domain.io.Logger;
import domain.repository.ConversionHistoryRepository;
import external.CurrConvExchangeService;
import external.CurrencyValidatorFromFile;
import repository.InMemoryConversionHistoryRepository;

import java.nio.file.Path;
import java.util.Scanner;

public class ConsoleRunner {

    public void run() {
        Scanner scanner = new Scanner(System.in);
        ExchangeService exchangeService = new CurrConvExchangeService();
        Logger logger = new ConsoleLogger();
        ConversionHistoryRepository conversionHistoryRepository = new InMemoryConversionHistoryRepository();
        CurrencyValidator currencyValidator = new CurrencyValidatorFromFile(Path.of("D:\\curencies.txt"));

        ConsoleCommandExecutor executor = new ConsoleCommandExecutor(
                exchangeService,
                conversionHistoryRepository,
                logger,
                currencyValidator
        );

        while (true) {
            String line = scanner.nextLine();
            String[] split = line.split("\\s+");

            executor.execute(split);
        }
    }
}
