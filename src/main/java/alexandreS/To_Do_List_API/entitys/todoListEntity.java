package alexandreS.To_Do_List_API.entitys;

import alexandreS.To_Do_List_API.Enus.StatusTodo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class todoListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private LocalDate dtaValidade;
    @Enumerated(EnumType.STRING)
    private StatusTodo status;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
        private usuarioEntity usuario;
}
