package alexandreS.To_Do_List_API.DTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa o login de um usuario")
public class usuarioLoginDTO {
    @NotBlank(message = "Email obrigatório")
    @Email(message = "Email precisa ser valido")
    @Schema(description = "Email do usuario",example = "usuario@email.com")
    private  String emailUsuario;

    @NotBlank(message = "Senha obrigatório")
    @Schema(description = "senha do usuario (minimo 8 caracters)",example = "senha123")
    private String senhaUsuario;
}
