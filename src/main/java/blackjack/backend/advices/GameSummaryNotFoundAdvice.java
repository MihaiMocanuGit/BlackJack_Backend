package blackjack.backend.advices;

import blackjack.backend.exceptions.GameSummaryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class GameSummaryNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(GameSummaryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(GameSummaryNotFoundException ex) {
        return ex.getMessage();
    }
}
