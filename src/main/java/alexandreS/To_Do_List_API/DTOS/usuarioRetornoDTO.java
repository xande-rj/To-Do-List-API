package alexandreS.To_Do_List_API.DTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Dto de retorno da rota usuario")
public class usuarioRetornoDTO {
    @Schema(description = "String de retorno do token")
    private String token;
}
