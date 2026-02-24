package alexandreS.To_Do_List_API.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class usuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true,nullable = false)
    private  String emailUsuario;

    private String nomeUsuario;
    private String senhaUsuario;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private ArrayList<todoListEntity> todoList = new ArrayList<>();


}
