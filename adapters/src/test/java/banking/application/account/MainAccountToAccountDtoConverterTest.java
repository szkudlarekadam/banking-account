package banking.application.account;

import static org.junit.jupiter.api.Assertions.*;

import banking.application.account.dto.AccountDto;
import banking.application.account.dto.MainAccountDto;
import banking.application.user.User;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MainAccountToAccountDtoConverterTest {

    private final MainAccountToAccountDtoConverter converter = new MainAccountToAccountDtoConverter();

    @Test
    void shouldConvert() {
        // given
        User user = new User();

        Account account = new Account();
        account.setId(2L);
        account.setCurrency(Currency.PLN);
        account.setBalance(BigDecimal.TEN);

        MainAccount mainAccount = new MainAccount();
        mainAccount.setId(1L);
        mainAccount.setBalance(BigDecimal.valueOf(1000));
        mainAccount.setCurrency(Currency.USD);
        mainAccount.setUser(user);
        mainAccount.getSubaccounts().add(account);

        // when
        MainAccountDto mainAccountDto = converter.convert(mainAccount);

        // then
        assertNotNull(mainAccountDto);
        assertEquals(1L, mainAccountDto.getId());
        assertEquals(Currency.USD.getValue(), mainAccountDto.getCurrency());
        assertEquals(1, mainAccountDto.getAccounts().size());
    }

}