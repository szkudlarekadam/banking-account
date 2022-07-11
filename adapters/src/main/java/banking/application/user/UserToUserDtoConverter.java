package banking.application.user;

import banking.application.account.MainAccountToAccountDtoConverter;
import banking.application.user.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class UserToUserDtoConverter implements Converter<User, UserDto> {

    private final MainAccountToAccountDtoConverter accountToAccountDtoConverter;

    public UserToUserDtoConverter(MainAccountToAccountDtoConverter accountToAccountDtoConverter) {
        this.accountToAccountDtoConverter = accountToAccountDtoConverter;
    }

    @Override
    public UserDto convert(User source) {
        return UserDto.builder()
              .name(source.getName())
              .surname(source.getSurname())
              .pesel(source.getPesel())
              .account(accountToAccountDtoConverter.convert(source.getAccount()))
              .build();
    }
}
