package alexandreS.To_Do_List_API.entitys;

import alexandreS.To_Do_List_API.Enus.StatusTodo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class todoListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTodo;

    private String titulo;
    private String descricao;
    private LocalDate dtaValidade;
    @Enumerated(EnumType.STRING)
    private StatusTodo status;

}
