package banking.application.account.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvertAmountInputDto {

    @NotNull
    private String pesel;

    @NotNull
    private long accountFromId;

    @NotNull
    private long accountToId;

    @NotNull
    @Positive
    private String amount;
}
