package banking.application.nbp;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class CurrencyExchangeRateRow {

    private String no;
    private LocalDate effectiveDate;

    @JsonProperty("mid")
    private String rate;

}
