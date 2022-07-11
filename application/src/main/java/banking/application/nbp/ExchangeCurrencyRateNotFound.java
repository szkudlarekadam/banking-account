package banking.application.nbp;

public class ExchangeCurrencyRateNotFound extends RuntimeException {

    public ExchangeCurrencyRateNotFound(String message) {
        super(message);
    }
}
