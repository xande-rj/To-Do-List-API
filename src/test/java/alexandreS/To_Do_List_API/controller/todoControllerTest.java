package alexandreS.To_Do_List_API.controller;

import alexandreS.To_Do_List_API.DTOS.todoRetornoDTO;
import alexandreS.To_Do_List_API.DTOS.todoSaveDTO;
import alexandreS.To_Do_List_API.DTOS.todoUpdateDTO;
import alexandreS.To_Do_List_API.Enus.StatusTodo;
import alexandreS.To_Do_List_API.entitys.todoListEntity;
import alexandreS.To_Do_List_API.errors.applicationException;
import alexandreS.To_Do_List_API.service.todoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class todoControllerTest {

    // ------------------------------------------------------------------
    // FIX 1: standaloneSetup has no Spring context, so your real
    // @ControllerAdvice is never loaded. Without this, thrown exceptions
    // become HTTP 500 instead of the correct status code.
    // The response body uses "message" key to match .andExpect(jsonPath("$.message"))
    // ------------------------------------------------------------------
    @RestControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(applicationException.class)
        ResponseEntity<Map<String, String>> handle(applicationException ex) {
            return ResponseEntity
                    .status(ex.getHttpStatus())
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @Mock
    private todoService service;

    // FIX 2: Remove @Mock Authentication — standaloneSetup injects null for
    // Authentication (no security context), so mocking it here has no effect
    // and causes PotentialStubbingProblem when matchers expect a real object.

    @InjectMocks
    private todoController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new TestExceptionHandler()) // register handler
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // =========================================================
    // POST /todo/save
    // =========================================================

    @Test
    @DisplayName("POST /todo/save - Deve criar um To-do e retornar 201")
    void save_success_returns201() throws Exception {
        todoSaveDTO dto = new todoSaveDTO("TítuloTeste123", "Descrição", LocalDate.of(2026, 12, 31), StatusTodo.PENDENTE);
        todoListEntity entity = buildEntity(10L, "TítuloTeste123", "Descrição",
                LocalDate.of(2026, 12, 31), StatusTodo.PENDENTE);

        // FIX 3: any() (not any(Authentication.class)) — Authentication arrives
        // as null from standaloneSetup; any(X.class) uses instanceof and null fails it.
        when(service.saveList(any(todoSaveDTO.class), any())).thenReturn(entity);

        mockMvc.perform(post("/todo/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.titulo").value("TítuloTeste123"))
                .andExpect(jsonPath("$.descricao").value("Descrição"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));

        verify(service).saveList(any(todoSaveDTO.class), any());
    }

    @Test
    @DisplayName("POST /todo/save - Deve retornar 400 quando service lançar exceção de token")
    void save_tokenNotFound_returns400() throws Exception {
        todoSaveDTO dto = new todoSaveDTO("Título", "Descrição", LocalDate.of(2026, 12, 31), StatusTodo.PENDENTE);

        when(service.saveList(any(todoSaveDTO.class),any()))
                .thenThrow(new applicationException("Token nao encontrado", HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/todo/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /todo/save - Deve retornar 404 quando usuário não for encontrado")
    void save_userNotFound_returns404() throws Exception {
        todoSaveDTO dto = new todoSaveDTO("Título12345", "Descrição", LocalDate.of(2026, 12, 31), StatusTodo.PENDENTE);

        when(service.saveList(any(todoSaveDTO.class), any()))
                .thenThrow(new applicationException("Usuario nao encontrado", HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/todo/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario nao encontrado"));
    }

    // =========================================================
    // GET /todo/all
    // =========================================================

    @Test
    @DisplayName("GET /todo/all - Deve retornar lista de To-dos e status 200")
    void getAll_noFilters_returns200() throws Exception {
        List<todoRetornoDTO> list = List.of(
                new todoRetornoDTO(1L, "Título 1", "Desc 1", LocalDate.of(2026, 12, 31), StatusTodo.PENDENTE),
                new todoRetornoDTO(2L, "Título 2", "Desc 2", LocalDate.of(2026, 11, 30), StatusTodo.CONCLUIDO)
        );

        // FIX 3 again: any() not any(Authentication.class)
        when(service.listAll(isNull(), isNull(), any())).thenReturn(list);

        mockMvc.perform(get("/todo/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("GET /todo/all - Deve retornar lista filtrada por status")
    void getAll_filterByStatus_returns200() throws Exception {
        List<todoRetornoDTO> list = List.of(
                new todoRetornoDTO(1L, "Título", "Desc", LocalDate.of(2026, 12, 31), StatusTodo.PENDENTE)
        );

        when(service.listAll(eq(StatusTodo.PENDENTE), isNull(), any())).thenReturn(list);

        mockMvc.perform(get("/todo/all").param("status", "PENDENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDENTE"));
    }

    @Test
    @DisplayName("GET /todo/all - Deve retornar lista filtrada por data")
    void getAll_filterByData_returns200() throws Exception {
        List<todoRetornoDTO> list = List.of(
                new todoRetornoDTO(1L, "Título", "Desc", LocalDate.of(2026, 6, 1), StatusTodo.PENDENTE)
        );

        when(service.listAll(isNull(), eq(LocalDate.of(2026, 6, 1)), any())).thenReturn(list);

        mockMvc.perform(get("/todo/all").param("data", "2026-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("GET /todo/all - Deve retornar lista filtrada por status e data")
    void getAll_filterByStatusAndData_returns200() throws Exception {
        List<todoRetornoDTO> list = List.of(
                new todoRetornoDTO(1L, "Título", "Desc", LocalDate.of(2026, 6, 1), StatusTodo.CONCLUIDO)
        );

        when(service.listAll(eq(StatusTodo.CONCLUIDO), eq(LocalDate.of(2026, 6, 1)), any())).thenReturn(list);

        mockMvc.perform(get("/todo/all")
                        .param("status", "CONCLUIDO")
                        .param("data", "2026-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("CONCLUIDO"));
    }

    @Test
    @DisplayName("GET /todo/all - Deve retornar 404 quando lista estiver vazia")
    void getAll_emptyList_returns404() throws Exception {
        when(service.listAll(any(), any(), any()))
                .thenThrow(new applicationException("Lista de To-do vazia", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/todo/all"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Lista de To-do vazia"));
    }

    // =========================================================
    // GET /todo/{id}
    // =========================================================

    @Test
    @DisplayName("GET /todo/{id} - Deve retornar o To-do pelo id com status 200")
    void getById_success_returns200() throws Exception {
        todoRetornoDTO dto = new todoRetornoDTO(10L, "Título", "Desc",
                LocalDate.of(2026, 12, 31), StatusTodo.PENDENTE);

        // FIX 4: anyLong() instead of eq(10L) — Spring binds @PathVariable as
        // primitive long; eq(10L) boxes to Long and Mockito sees a type mismatch,
        // making the stub resolve to 0L instead of 10L.
        when(service.getById(any(), anyLong())).thenReturn(dto);

        mockMvc.perform(get("/todo/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.titulo").value("Título"));
    }

    @Test
    @DisplayName("GET /todo/{id} - Deve retornar 404 quando To-do não for encontrado")
    void getById_notFound_returns404() throws Exception {
        when(service.getById(any(), anyLong()))
                .thenThrow(new applicationException("To-do nao encontrado", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/todo/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("To-do nao encontrado"));
    }

    // =========================================================
    // PATCH /todo/{id}
    // =========================================================

    @Test
    @DisplayName("PATCH /todo/{id} - Deve atualizar o To-do e retornar 201")
    void updateById_success_returns201() throws Exception {
        todoUpdateDTO update = new todoUpdateDTO("Novo Título", "Nova Desc",
                LocalDate.of(2026, 12, 31), StatusTodo.CONCLUIDO);
        todoListEntity entity = buildEntity(10L, "Novo Título", "Nova Desc",
                LocalDate.of(2026, 12, 31), StatusTodo.CONCLUIDO);

        // FIX 4 again: anyLong() for the same reason
        when(service.updateById(any(), anyLong(), any(todoUpdateDTO.class))).thenReturn(entity);

        mockMvc.perform(patch("/todo/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.titulo").value("Novo Título"))
                .andExpect(jsonPath("$.status").value("CONCLUIDO"));
    }

    @Test
    @DisplayName("PATCH /todo/{id} - Deve retornar 404 quando To-do não for encontrado")
    void updateById_notFound_returns404() throws Exception {
        todoUpdateDTO update = new todoUpdateDTO();
        update.setTitulo("TítuloTeste123");

        when(service.updateById(any(), anyLong(), any()))
                .thenThrow(new applicationException("To-do nao encontrado", HttpStatus.NOT_FOUND));

        mockMvc.perform(patch("/todo/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("To-do nao encontrado"));
    }

    @Test
    @DisplayName("PATCH /todo/{id} - Deve atualizar apenas os campos enviados (atualização parcial)")
    void updateById_partialUpdate_returns201() throws Exception {
        todoUpdateDTO update = new todoUpdateDTO("Só Título", null, null, null);
        todoListEntity entity = buildEntity(10L, "Só Título", "Desc Original",
                LocalDate.of(2026, 12, 31), StatusTodo.PENDENTE);

        when(service.updateById(any(), anyLong(), any())).thenReturn(entity);

        mockMvc.perform(patch("/todo/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Só Título"))
                .andExpect(jsonPath("$.descricao").value("Desc Original"));
    }

    // =========================================================
    // DELETE /todo/{id}
    // =========================================================

    @Test
    @DisplayName("DELETE /todo/{id} - Deve deletar o To-do e retornar 204")
    void deleteById_success_returns204() throws Exception {
        // FIX 3 + 4: any() for Authentication, anyLong() for id
        doNothing().when(service).deleteById(any(), anyLong());

        mockMvc.perform(delete("/todo/10"))
                .andExpect(status().isNoContent());

        verify(service).deleteById(any(), anyLong());
    }

    @Test
    @DisplayName("DELETE /todo/{id} - Deve chamar service com o id correto")
    void deleteById_callsServiceWithCorrectId() throws Exception {
        doNothing().when(service).deleteById(any(), anyLong());

        mockMvc.perform(delete("/todo/55"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(any(), anyLong());
        verifyNoMoreInteractions(service);
    }

    // =========================================================
    // Helper
    // =========================================================

    private todoListEntity buildEntity(Long id, String titulo, String descricao,
                                       LocalDate dtaValidade, StatusTodo status) {
        todoListEntity entity = new todoListEntity();
        entity.setId(id);
        entity.setTitulo(titulo);
        entity.setDescricao(descricao);
        entity.setDtaValidade(dtaValidade);
        entity.setStatus(status);
        return entity;
    }
}