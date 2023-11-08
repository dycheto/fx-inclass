package domain.external;

import domain.entities.Money;

public interface ExchangeService {

    Money exchange(Money convertFrom, String toCurrency);

}
