package banking.application.account.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepositInputDto {
    @NotNull
    @Positive
    private String amount;
    @NotNull
    private String accountId;
    @NotNull
    private String currency;
}
