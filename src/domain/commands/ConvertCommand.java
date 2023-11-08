package domain.commands;

import domain.entities.Money;
import domain.external.ExchangeService;
import domain.io.Logger;

public class ConvertCommand implements Command<ConvertCommand.Input> {

    private final ExchangeService exchangeService;
    private final Logger logger;

    public ConvertCommand(ExchangeService exchangeService, Logger logger) {
        this.exchangeService = exchangeService;
        this.logger = logger;
    }

    @Override
    public void execute(Input input) {
        this.exchangeAndLog(input);
    }

    protected Money exchangeAndLog(Input input){
        Money convertedMoney = exchangeService.exchange(input.convertFrom, input.toCurrency);
        logger.logLine(convertedMoney.toString());
        return convertedMoney;
    }

    public static class Input extends EmptyInput {
        private final Money convertFrom;
        private final String toCurrency;

        public Input(Money convertFrom, String toCurrency) {
            this.convertFrom = convertFrom;
            this.toCurrency = toCurrency;
        }

        public Money getConvertFrom() {
            return convertFrom;
        }

        public String getToCurrency() {
            return toCurrency;
        }
    }

}
