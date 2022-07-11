package banking.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
class ExceptionResponse {

    private final String message;
}
