package banking.application.user;

import banking.application.user.dto.CreateUserInputDto;
import banking.application.user.dto.UserDto;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
class UserEndpoint {
    private final UserRepository userRepository;
    private final UserFacade userFacade;
    private final UserDtoToUserConverter userDtoToUserConverter;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public UserEndpoint(UserRepository userRepository, UserFacade userFacade, UserDtoToUserConverter userDtoToUserConverter,
          UserToUserDtoConverter userToUserDtoConverter) {
        this.userRepository = userRepository;
        this.userFacade = userFacade;
        this.userDtoToUserConverter = userDtoToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @PostMapping
    @ResponseBody UserDto createUser(@Valid @RequestBody CreateUserInputDto createUserInputDto) {
        User user = userFacade.save(userDtoToUserConverter.convert(createUserInputDto));
        return userToUserDtoConverter.convert(user);
    }

    @GetMapping("/{pesel}")
    @ResponseBody UserDto getUserInfo(@PathVariable String pesel) {
        Optional<User> user = userRepository.findByPesel(pesel);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with pesel: " + pesel);
        }

        return userToUserDtoConverter.convert(user.get());
    }


}
