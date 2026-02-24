package alexandreS.To_Do_List_API.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private List<todoListEntity> todoList= new ArrayList<>() ;


}
