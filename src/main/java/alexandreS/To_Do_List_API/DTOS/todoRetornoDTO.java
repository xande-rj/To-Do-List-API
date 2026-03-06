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
@Schema(description = "Representa retorno tarefa da lista (To-Do)")

public class todoRetornoDTO {
    @Schema(description = "ID único da tarefa", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
private  Long id;

    @Schema(description = "Título da tarefa", example = "Comprar leite", required = true)
    private String titulo;

    @Schema(description = "Descrição detalhada", example = "Ir ao mercado e comprar leite desnatado")
    private String descricao;

    @Schema(description = "Data de validade (limite para conclusão)", example = "2026-12-31")
    private LocalDate dtaValidade;

    @Schema(description = "Status atual da tarefa", allowableValues = {"PENDENTE", "CONCLUIDO"})
    private StatusTodo status ;
}
