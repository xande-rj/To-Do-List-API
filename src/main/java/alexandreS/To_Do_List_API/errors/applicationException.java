package alexandreS.To_Do_List_API.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class applicationException extends  RuntimeException{

    private final String message;
    private final HttpStatus httpStatus;
}
