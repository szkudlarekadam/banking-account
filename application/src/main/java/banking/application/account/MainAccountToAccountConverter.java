package banking.application.account;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
class MainAccountToAccountConverter implements Converter<MainAccount, Account> {

    @Override
    public Account convert(MainAccount source) {
        Account account = new Account();
        account.setId(source.getId());
        account.setCurrency(source.getCurrency());
        account.setBalance(source.getBalance());
        account.setAccount(source);
        return account;
    }
}
