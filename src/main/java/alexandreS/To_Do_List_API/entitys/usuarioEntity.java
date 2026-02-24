package alexandreS.To_Do_List_API.entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class usuarioEntity {
    @Id
    private  String emailUsuario;

    private String nomeUsuario;
    private String senhaUsuario;
    private ArrayList<Integer> todoListId;


}
