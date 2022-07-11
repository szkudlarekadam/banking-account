package banking.application.account.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountInputDto {

    private String pesel;
    private String currency;

}
