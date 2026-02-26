package alexandreS.To_Do_List_API.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class usuarioLoginDTO {
    @NotBlank
    private  String emailUsuario;
    @NotBlank
    private String senhaUsuario;
}
