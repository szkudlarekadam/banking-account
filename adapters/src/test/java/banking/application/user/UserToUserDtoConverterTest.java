package banking.application.user;

import static org.junit.jupiter.api.Assertions.*;

import banking.application.account.AccountToAccountDtoConverter;
import banking.application.account.MainAccount;
import banking.application.account.MainAccountToAccountDtoConverter;
import banking.application.account.dto.AccountDto;
import banking.application.account.dto.MainAccountDto;
import banking.application.user.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserToUserDtoConverterTest {

    @Mock
    private MainAccountToAccountDtoConverter accountToAccountDtoConverter;

    @InjectMocks
    private UserToUserDtoConverter converter;

    @Test
    void shouldConvert() {
        // given
        User user = new User();
        user.setName("Adam");
        user.setSurname("Szkudlarek");
        user.setPesel("12345678901");
        user.setAccount(new MainAccount());

        // when
        Mockito.when(accountToAccountDtoConverter.convert(Mockito.any())).thenReturn(new MainAccountDto());
        UserDto userDto = converter.convert(user);

        // then
        assertNotNull(userDto);
        assertEquals("Adam", userDto.getName());
        assertEquals("Szkudlarek", userDto.getSurname());
        assertEquals("12345678901", userDto.getPesel());
        assertNotNull(user.getAccount());
    }

}