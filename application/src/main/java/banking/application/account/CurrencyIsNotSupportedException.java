package banking.application.account;

public class CurrencyIsNotSupportedException extends RuntimeException {

    public CurrencyIsNotSupportedException(String message) {
        super(message);
    }
}
