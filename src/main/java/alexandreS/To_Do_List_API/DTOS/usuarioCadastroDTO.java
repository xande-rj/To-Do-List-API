package alexandreS.To_Do_List_API.DTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa o cadastro do usuario")
public class usuarioCadastroDTO {

    @NotBlank(message = "Email obrigatório")
    @Email(message = "Email precisa ser valido")
    @Schema(description = "Email do usuario",example = "usuario@email.com")
    private  String emailUsuario;

    @NotBlank(message = "Nome obrigatório")
    @Schema(description = "Nome do usuario",example = "nome usuario")
    private String nomeUsuario;

    @Size(min = 8,message = "A senha precisa de no minino 8 caracteres")
    @NotBlank(message = "Senha obrigatório")
    @Schema(description = "Senha do usario (minimo 8 caracters)",example = "senha123")
    private String senhaUsuario;


}
