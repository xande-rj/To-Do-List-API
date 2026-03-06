package alexandreS.To_Do_List_API.DTOS;
import alexandreS.To_Do_List_API.Enus.StatusTodo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Dados para atualização parcial de uma tarefa")
public class todoUpdateDTO {

    @Schema(description = "Novo título da tarefa", example = "Comprar pão")
    private String titulo;

    @Schema(description = "Nova descrição", example = "Ir à padaria e comprar pão francês")
    private String descricao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "A data não pode estar no passado")
    @Schema(description = "Nova data de validade", example = "2025-08-31")
    private LocalDate dtaValidade;

    @Schema(description = "Novo status", allowableValues = {"PENDENTE", "CONCLUIDO"})
    private StatusTodo status;
}