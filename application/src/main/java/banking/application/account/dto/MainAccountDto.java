package banking.application.account.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainAccountDto {

    private long id;
    private BigDecimal balance;
    private String currency;
    private List<AccountDto> accounts;

}
