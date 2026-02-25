package alexandreS.To_Do_List_API.errors;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class applicationExceptionHandle extends ResponseEntityExceptionHandler {

    @ExceptionHandler(applicationException.class)
    public ResponseEntity<apiErroResponse> ApplicationErro(final applicationException exception, final HttpServletRequest request){

        log.error(String.format("error message: %s",exception.getMessage()),exception);

        apiErroResponse response = new apiErroResponse(
                exception.getMessage(),
                exception.getHttpStatus().value(),
                exception.getHttpStatus().name(),
                request.getMethod(),
                LocalDateTime.now()
        );
        return  new ResponseEntity<apiErroResponse>(response,exception.getHttpStatus());
    }
}
