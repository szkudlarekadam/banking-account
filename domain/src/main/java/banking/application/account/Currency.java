package banking.application.account;

import lombok.Getter;

@Getter
public enum Currency {

    PLN("PLN"), USD("USD"), EUR("EUR");

    private final String value;

    Currency(String value) {
        this.value = value;
    }

    public static Currency findByName(String name) {
        Currency result = null;
        for (Currency currency : values()) {
            if (name.equals(currency.getValue())) {
                result = currency;
                break;
            }
        }

        return result;
    }
}
