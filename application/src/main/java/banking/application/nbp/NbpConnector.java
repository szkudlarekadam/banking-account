package banking.application.nbp;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class NbpConnector {

    // TODO: Nice to have time cache
    public BigDecimal getExchangeRate(String currency) {
        String url = "http://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/";
        RestTemplate restTemplate = new RestTemplate();
        CurrencyExchangeRate result;
        try {
            result = restTemplate.getForObject(url, CurrencyExchangeRate.class);
            if (result == null || result.getRates().isEmpty()) {
                throw new ExchangeCurrencyRateNotFound("Exchange rate for currency: " + currency + " not found");
            }
        } catch (HttpClientErrorException | IllegalArgumentException ex) {
            throw new ExchangeCurrencyRateNotFound("Exchange rate for currency: " + currency + " not found");
        }

        return new BigDecimal(result.getRates().get(0).getRate());

    }



}
