package banking.application.account;

import banking.application.account.dto.AccountDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountToAccountDtoConverter implements Converter<Account, AccountDto> {

    @Override
    public AccountDto convert(Account source) {
        return AccountDto.builder()
              .id(source.getId())
              .currency(source.getCurrency().getValue())
              .balance(source.getBalance())
              .build();
    }
}
