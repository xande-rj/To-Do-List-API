package alexandreS.To_Do_List_API.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class usuarioLoginDTO {
    @NotBlank(message = "Email obrigatório")
    @Email(message = "Email precisa ser valido")
    private  String emailUsuario;
    @NotBlank(message = "Senha obrigatório")
    private String senhaUsuario;
}
