package alexandreS.To_Do_List_API.DTO;

import alexandreS.To_Do_List_API.Enus.StatusTodo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class todoDTO {
    private String titulo;
    private String descricao;
    private LocalDate dtaValidade;
    private StatusTodo status;
}
