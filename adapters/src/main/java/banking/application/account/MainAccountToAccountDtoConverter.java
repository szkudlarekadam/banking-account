package banking.application.account;

import banking.application.account.dto.AccountDto;
import banking.application.account.dto.MainAccountDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MainAccountToAccountDtoConverter implements Converter<MainAccount, MainAccountDto> {

    @Override
    public MainAccountDto convert(MainAccount source) {
        return MainAccountDto.builder()
              .id(source.getId())
              .balance(source.getBalance())
              .currency(source.getCurrency().getValue())
              .accounts(convertAccounts(source.getSubaccounts()))
              .build();
    }

    private List<AccountDto> convertAccounts(List<Account> accounts) {
        return accounts.stream()
              .map(this::convertSingleAccount)
              .collect(Collectors.toList());
    }

    private AccountDto convertSingleAccount(Account account) {
        return AccountDto.builder()
              .id(account.getId())
              .balance(account.getBalance())
              .currency(account.getCurrency().getValue())
              .build();
    }
}
