package banking.application.user;

import banking.application.account.MainAccount;
import banking.application.user.dto.CreateUserInputDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class UserDtoToUserConverter implements Converter<CreateUserInputDto, User> {

    @Override
    public User convert(CreateUserInputDto source) {
        User user = new User();
        user.setName(source.getName());
        user.setSurname(source.getSurname());
        user.setPesel(source.getPesel());
        user.setAccount(new MainAccount());
        user.getAccount().setBalance(source.getStartBalance());

        return user;
    }
}
