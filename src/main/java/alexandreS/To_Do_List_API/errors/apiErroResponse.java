package alexandreS.To_Do_List_API.errors;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class apiErroResponse {

    private  final String message;
    private  final int statusCode;
    private  final String statusName;
    private  final String method;
    private  final LocalDateTime timestamp;
}

