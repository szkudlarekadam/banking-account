package banking.application.user.dto;

import banking.application.account.dto.MainAccountDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private String name;
    private String surname;
    private String pesel;
    private MainAccountDto account;
}
