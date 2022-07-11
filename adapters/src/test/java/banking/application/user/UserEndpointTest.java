package banking.application.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import banking.application.BankingApplication;
import banking.application.GlobalExceptionHandler;
import banking.application.user.dto.CreateUserInputDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(classes = BankingApplication.class)
class UserEndpointTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserFacade userFacade;

    @MockBean
    private UserDtoToUserConverter userDtoToUserConverter;

    @MockBean
    private UserToUserDtoConverter userToUserDtoConverter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void check200StatusOnCorrectMessageOnUserCreate() throws Exception {
        // given
        CreateUserInputDto createUserInputDto = CreateUserInputDto.builder()
              .name("Adam")
              .surname("Szkudlarek")
              .pesel("12345678901")
              .startBalance(new BigDecimal("2000"))
              .build();

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
              new UserEndpoint(userRepository, userFacade, userDtoToUserConverter, userToUserDtoConverter))
              .setControllerAdvice(new GlobalExceptionHandler())
              .build();

        // when
        MvcResult result = mockMvc
              .perform(post("/user")
              .content(objectMapper.writeValueAsString(createUserInputDto))
              .contentType(MediaType.APPLICATION_JSON))
          .andReturn();

        // then
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    void check200StatusOnCorrectMessageGetUserInfo() throws Exception {
        // given

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
                    new UserEndpoint(userRepository, userFacade, userDtoToUserConverter, userToUserDtoConverter))
              .setControllerAdvice(new GlobalExceptionHandler())
              .build();

        // when
        MvcResult result = mockMvc
              .perform(get("/user/12345678901"))
              .andReturn();

        // then
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

}