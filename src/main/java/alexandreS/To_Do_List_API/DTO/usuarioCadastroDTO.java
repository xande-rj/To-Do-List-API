package alexandreS.To_Do_List_API.DTO;

import alexandreS.To_Do_List_API.entitys.todoListEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class usuarioCadastroDTO {

    @NotBlank(message = "Email obrigatório")
    @Email(message = "Email precisa ser valido")
    private  String emailUsuario;

    @NotBlank(message = "Nome obrigatório")
    private String nomeUsuario;

    @Size(min = 8,message = "A senha precisa de no minino 8 caracteres")
    @NotBlank(message = "Senha obrigatório")
    private String senhaUsuario;


}
