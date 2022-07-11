package banking.application.nbp;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyExchangeRate {

    private String table;
    private String currency;
    private String code;
    private List<CurrencyExchangeRateRow> rates;

}
