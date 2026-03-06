package alexandreS.To_Do_List_API.DTOS;

import alexandreS.To_Do_List_API.Enus.StatusTodo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa uma tarefa da lista (To-Do)")
public class todoSaveDTO {

    @Size(min = 10, max = 40, message = "O titulo da tarefa deve ter no minino 10 letras e maximo 40")
    @NotBlank(message = "A titulo é obrigatória")
    @Schema(description = "Título da tarefa", example = "Comprar leite")
    private String titulo;

    @NotBlank(message = "A descricao é obrigatória")
    @Schema(description = "Descrição detalhada", example = "Ir ao mercado e comprar leite desnatado")
    private String descricao;

    @NotNull(message = "A data é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "A data não pode estar no passado")
    @Schema(description = "Data de validade (limite para conclusão)", example = "2026-07-31")
    private LocalDate dtaValidade;

    @Schema(description = "Status atual da tarefa", allowableValues = {"PENDENTE", "CONCLUIDO"})
    private StatusTodo status;

}
