package external;

import domain.entities.Money;
import domain.external.ExchangeService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrConvExchangeService implements ExchangeService {

    private static final String API_KEY = System.getenv("API_KEY");

    @Override
    public Money exchange(Money from, String toCurrency) {

        BigDecimal rate = fetchExchangeRateFor(from.getCurrency(), toCurrency);
        BigDecimal exchangedValue = from.getValue().multiply(rate);
        return new Money(exchangedValue, toCurrency);
    }

    private BigDecimal fetchExchangeRateFor(String currencyFrom, String currencyTo) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(echangeRateUrlForCurrencies(currencyFrom, currencyTo))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return extractExchangeRate(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong during HTTP request to external API");
        }

    }

    private BigDecimal extractExchangeRate(String response) {
        int columnIndx = response.lastIndexOf(':');
        int closingBracket = response.lastIndexOf('}');
        String exchangeRate = response.substring(columnIndx + 1, closingBracket);

        return new BigDecimal(exchangeRate);
    }

    private URI echangeRateUrlForCurrencies(String from, String to) {
        String url = String.format("https://free.currconv.com/api/v7/convert?apiKey=%s&q=%s_%s&compact=ultra", API_KEY, from, to);

        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("failed to build URI");
        }
    }

//    public static void main(String[] args) {
//        var service = new CurrConvExchangeService();
//
//        Money exchanged = service.exchange(new Money(new BigDecimal("10"), "BGN"), "USD");
//
//    }

}
