package console;

import domain.commands.*;
import domain.entities.Money;
import domain.external.CurrencyValidator;
import domain.external.ExchangeService;
import domain.io.Logger;
import domain.repository.ConversionHistoryRepository;

import java.math.BigDecimal;

public class ConsoleCommandExecutor {
    ExchangeService exchangeService;
    ConversionHistoryRepository conversionHistoryRepository;
    Logger logger;
    CurrencyValidator validator;

    public ConsoleCommandExecutor(ExchangeService exchangeService,
                                  ConversionHistoryRepository conversionHistoryRepository,
                                  Logger logger,
                                  CurrencyValidator validator) {
        this.exchangeService = exchangeService;
        this.conversionHistoryRepository = conversionHistoryRepository;
        this.logger = logger;
    }

    public void execute(String[] args) {
        switch (args[0]) {
            case "END":
                end();
                break;
            case "CONVERT":
                convert(args);
                break;
            case "HISTORY":
                history(args);
                break;
            default:
                logger.logLine("Incorrect command");
        }
    }

    private void end() {
        new EndCommand().execute(new EmptyInput());
    }

    private void convert(String[] split) {
        BigDecimal fromValue = new BigDecimal(split[1]);
        String fromCurrency = split[2];
        String toCurrency = split[3];

        ConvertCommand.Input input = new ConvertCommand.Input(
                new Money(fromValue, fromCurrency),
                toCurrency
        );

        new ValidatedHistorySavingConvertCommand(exchangeService, logger, conversionHistoryRepository, validator).execute(input);
    }

    private void history(String[] split) {
        Command<HistoryCommand.Input> cmd = new HistoryCommand(conversionHistoryRepository, logger);
        cmd.execute(new HistoryCommand.Input(Integer.parseInt(split[1])));
    }

}
