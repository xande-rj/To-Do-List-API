package alexandreS.To_Do_List_API.DTO;

import alexandreS.To_Do_List_API.entitys.todoListEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class usuarioDTO {
    private  String emailUsuario;
    private String nomeUsuario;
    private String senhaUsuario;
    private List<todoListEntity> todoList= new ArrayList<>() ;
}
