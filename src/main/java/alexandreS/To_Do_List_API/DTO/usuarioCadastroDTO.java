package alexandreS.To_Do_List_API.DTO;

import alexandreS.To_Do_List_API.entitys.todoListEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class usuarioCadastroDTO {
    @NotBlank
    @NotNull(message = "Email obrigatório")
    private  String emailUsuario;
    @NotBlank
    @NotNull(message = "Nome obrigatório")
    private String nomeUsuario;

    @NotBlank
    @NotNull(message = "Senha obrigatório")
    private String senhaUsuario;

    private List<todoListEntity> todoList= new ArrayList<>() ;
}
