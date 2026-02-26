package alexandreS.To_Do_List_API.DTO;

import alexandreS.To_Do_List_API.Enus.StatusTodo;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class todoDTO {
    @NotNull(message = "A titulo é obrigatória")
    private String titulo;

    @NotNull(message = "A descricao é obrigatória")
    private String descricao;

    @NotNull(message = "A data é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "A data não pode estar no passado")
    private LocalDate dtaValidade;

    @NotNull(message = "A status é obrigatória")
    private StatusTodo status;
}
