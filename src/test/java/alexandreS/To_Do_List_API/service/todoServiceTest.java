package alexandreS.To_Do_List_API.service;

import alexandreS.To_Do_List_API.DTOS.todoRetornoDTO;
import alexandreS.To_Do_List_API.DTOS.todoSaveDTO;
import alexandreS.To_Do_List_API.DTOS.todoUpdateDTO;
import alexandreS.To_Do_List_API.Enus.StatusTodo;
import alexandreS.To_Do_List_API.entitys.todoListEntity;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.errors.applicationException;
import alexandreS.To_Do_List_API.repository.todoRepository;
import alexandreS.To_Do_List_API.repository.usuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class todoServiceTest {

    @Mock
    private todoRepository repository;

    @Mock
    private usuarioRepository userRepository;

    @Mock
    Authentication authentication;

    private todoService service;

    @BeforeEach
    void setUp() {
        this.service = new todoService(repository, userRepository);
    }

    // =========================================================
    // saveList
    // =========================================================

    @Test
    @DisplayName("Deve criar um To-do com sucesso")
    void saveList_success() {
        Long usuarioId = 1L;
        todoSaveDTO dto = new todoSaveDTO("Título", "Descrição", LocalDate.now(), StatusTodo.PENDENTE);

        when(authentication.getName()).thenReturn(usuarioId.toString());

        usuarioEntity usuarioE = new usuarioEntity();
        usuarioE.setId(usuarioId);
        when(userRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioE));

        todoListEntity entitySaved = new todoListEntity();
        entitySaved.setId(10L);
        when(repository.save(any(todoListEntity.class))).thenReturn(entitySaved);

        todoListEntity resultado = service.saveList(dto, authentication);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(10L);
        verify(authentication).getName();
        verify(userRepository).findById(usuarioId);
        verify(repository).save(any(todoListEntity.class));
    }

    @Test
    @DisplayName("Deve usar StatusTodo.PENDENTE quando status do DTO for nulo")
    void saveList_nullStatus_defaultsPendente() {
        Long usuarioId = 1L;
        todoSaveDTO dto = new todoSaveDTO("Título", "Descrição", LocalDate.now(), null);

        when(authentication.getName()).thenReturn(usuarioId.toString());

        usuarioEntity usuarioE = new usuarioEntity();
        usuarioE.setId(usuarioId);
        when(userRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioE));

        when(repository.save(any(todoListEntity.class))).thenAnswer(invocation -> {
            todoListEntity saved = invocation.getArgument(0);
            assertThat(saved.getStatus()).isEqualTo(StatusTodo.PENDENTE);
            return saved;
        });

        service.saveList(dto, authentication);

        verify(repository).save(any(todoListEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando authentication for nulo no saveList")
    void saveList_nullAuthentication_throwsException() {
        todoSaveDTO dto = new todoSaveDTO("Título", "Descrição", LocalDate.now(), StatusTodo.PENDENTE);

        assertThatThrownBy(() -> service.saveList(dto, null))
                .isInstanceOf(applicationException.class)
                .hasMessageContaining("Token nao encontrado");

        verifyNoInteractions(repository, userRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado no saveList")
    void saveList_userNotFound_throwsException() {
        Long usuarioId = 1L;
        todoSaveDTO dto = new todoSaveDTO("Título", "Descrição", LocalDate.now(), StatusTodo.PENDENTE);

        when(authentication.getName()).thenReturn(usuarioId.toString());
        when(userRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.saveList(dto, authentication))
                .isInstanceOf(applicationException.class)
                .hasMessageContaining("Usuario nao encontrado");

        verifyNoInteractions(repository);
    }

    // =========================================================
    // listAll
    // =========================================================

    @Test
    @DisplayName("Deve listar todos os to-dos sem filtro de status e data")
    void listAll_noFilters_returnsAll() {
        Long usuarioId = 1L;
        when(authentication.getName()).thenReturn(usuarioId.toString());

        List<todoListEntity> entities = buildEntityList();
        when(repository.findByUsuarioId(usuarioId)).thenReturn(entities);

        List<todoRetornoDTO> resultado = service.listAll(null, null, authentication);

        assertThat(resultado).isNotNull().hasSize(1);
        verify(repository).findByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista sem filtros estiver vazia")
    void listAll_noFilters_emptyList_throwsException() {
        Long usuarioId = 1L;
        when(authentication.getName()).thenReturn(usuarioId.toString());
        when(repository.findByUsuarioId(usuarioId)).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> service.listAll(null, null, authentication))
                .isInstanceOf(applicationException.class)
                .hasMessageContaining("Lista de To-do vazia");
    }

    @Test
    @DisplayName("Deve listar todos os to-dos filtrando apenas por status")
    void listAll_onlyStatus_returnsFiltered() {
        Long usuarioId = 1L;
        when(authentication.getName()).thenReturn(usuarioId.toString());

        List<todoListEntity> entities = buildEntityList();
        when(repository.findByUsuarioIdAndStatus(usuarioId, StatusTodo.PENDENTE)).thenReturn(entities);

        List<todoRetornoDTO> resultado = service.listAll(StatusTodo.PENDENTE, null, authentication);

        assertThat(resultado).isNotNull().hasSize(1);
        verify(repository).findByUsuarioIdAndStatus(usuarioId, StatusTodo.PENDENTE);
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista por status estiver vazia")
    void listAll_onlyStatus_emptyList_throwsException() {
        Long usuarioId = 1L;
        when(authentication.getName()).thenReturn(usuarioId.toString());
        when(repository.findByUsuarioIdAndStatus(usuarioId, StatusTodo.PENDENTE)).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> service.listAll(StatusTodo.PENDENTE, null, authentication))
                .isInstanceOf(applicationException.class)
                .hasMessageContaining("Lista de To-do vazia");
    }

    @Test
    @DisplayName("Deve listar todos os to-dos filtrando apenas por data")
    void listAll_onlyData_returnsFiltered() {
        Long usuarioId = 1L;
        LocalDate data = LocalDate.now();
        when(authentication.getName()).thenReturn(usuarioId.toString());

        List<todoListEntity> entities = buildEntityList();
        entities.get(0).setDtaValidade(data);
        when(repository.findByUsuarioIdAndDtaValidadeLessThanEqualOrderByDtaValidadeAsc(usuarioId, data))
                .thenReturn(entities);

        List<todoRetornoDTO> resultado = service.listAll(null, data, authentication);

        assertThat(resultado).isNotNull().hasSize(1);
        verify(repository).findByUsuarioIdAndDtaValidadeLessThanEqualOrderByDtaValidadeAsc(usuarioId, data);
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista por data estiver vazia")
    void listAll_onlyData_emptyList_throwsException() {
        Long usuarioId = 1L;
        LocalDate data = LocalDate.now();
        when(authentication.getName()).thenReturn(usuarioId.toString());
        when(repository.findByUsuarioIdAndDtaValidadeLessThanEqualOrderByDtaValidadeAsc(usuarioId, data))
                .thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> service.listAll(null, data, authentication))
                .isInstanceOf(applicationException.class)
                .hasMessageContaining("Lista de To-do vazia");
    }

    @Test
    @DisplayName("Deve listar todos os to-dos filtrando por status e data")
    void listAll_statusAndData_returnsFiltered() {
        Long usuarioId = 1L;
        LocalDate data = LocalDate.now();
        when(authentication.getName()).thenReturn(usuarioId.toString());

        List<todoListEntity> entities = buildEntityList();
        entities.get(0).setStatus(StatusTodo.PENDENTE);
        entities.get(0).setDtaValidade(data);
        when(repository.findByUsuarioIdAndStatusAndDtaValidadeLessThanEqual(usuarioId, StatusTodo.PENDENTE, data))
                .thenReturn(entities);

        List<todoRetornoDTO> resultado = service.listAll(StatusTodo.PENDENTE, data, authentication);

        assertThat(resultado).isNotNull().hasSize(1);
        verify(repository).findByUsuarioIdAndStatusAndDtaValidadeLessThanEqual(usuarioId, StatusTodo.PENDENTE, data);
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista por status e data estiver vazia")
    void listAll_statusAndData_emptyList_throwsException() {
        Long usuarioId = 1L;
        LocalDate data = LocalDate.now();
        when(authentication.getName()).thenReturn(usuarioId.toString());
        when(repository.findByUsuarioIdAndStatusAndDtaValidadeLessThanEqual(usuarioId, StatusTodo.PENDENTE, data))
                .thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> service.listAll(StatusTodo.PENDENTE, data, authentication))
                .isInstanceOf(applicationException.class)
                .hasMessageContaining("Lista de To-do vazia");
    }

    // =========================================================
    // getById
    // =========================================================

    @Test
    @DisplayName("Deve retornar um to-do pelo id com sucesso")
    void getById_success() {
        Long usuarioId = 1L;
        Long todoId = 10L;
        when(authentication.getName()).thenReturn(usuarioId.toString());

        todoListEntity entity = buildEntity(todoId);
        when(repository.findByIdAndUsuarioId(todoId, usuarioId)).thenReturn(Optional.of(entity));

        todoRetornoDTO resultado = service.getById(authentication, todoId);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(todoId);
        verify(repository).findByIdAndUsuarioId(todoId, usuarioId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando to-do não for encontrado pelo id")
    void getById_notFound_throwsException() {
        Long usuarioId = 1L;
        Long todoId = 10L;
        when(authentication.getName()).thenReturn(usuarioId.toString());
        when(repository.findByIdAndUsuarioId(todoId, usuarioId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(authentication, todoId))
                .isInstanceOf(applicationException.class)
                .hasMessageContaining("To-do nao encontrado");
    }

    // =========================================================
    // updateById
    // =========================================================

    @Test
    @DisplayName("Deve atualizar todos os campos do to-do com sucesso")
    void updateById_allFields_success() {
        Long usuarioId = 1L;
        Long todoId = 10L;
        when(authentication.getName()).thenReturn(usuarioId.toString());

        todoListEntity entity = buildEntity(todoId);
        when(repository.findByIdAndUsuarioId(todoId, usuarioId)).thenReturn(Optional.of(entity));
        when(repository.save(any(todoListEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        todoUpdateDTO updates = new todoUpdateDTO("Novo Título", "Nova Descrição", LocalDate.now().plusDays(1), StatusTodo.CONCLUIDO);

        todoListEntity resultado = service.updateById(authentication, todoId, updates);

        assertThat(resultado.getTitulo()).isEqualTo("Novo Título");
        assertThat(resultado.getDescricao()).isEqualTo("Nova Descrição");
        assertThat(resultado.getStatus()).isEqualTo(StatusTodo.CONCLUIDO);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos não nulos do to-do")
    void updateById_partialUpdate_onlyNonNullFields() {
        Long usuarioId = 1L;
        Long todoId = 10L;
        when(authentication.getName()).thenReturn(usuarioId.toString());

        todoListEntity entity = buildEntity(todoId);
        entity.setTitulo("Título Original");
        entity.setDescricao("Descrição Original");
        entity.setStatus(StatusTodo.PENDENTE);
        when(repository.findByIdAndUsuarioId(todoId, usuarioId)).thenReturn(Optional.of(entity));
        when(repository.save(any(todoListEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        // Only titulo is updated; other fields are null
        todoUpdateDTO updates = new todoUpdateDTO("Título Atualizado", null, null, null);

        todoListEntity resultado = service.updateById(authentication, todoId, updates);

        assertThat(resultado.getTitulo()).isEqualTo("Título Atualizado");
        assertThat(resultado.getDescricao()).isEqualTo("Descrição Original");
        assertThat(resultado.getStatus()).isEqualTo(StatusTodo.PENDENTE);
    }

    @Test
    @DisplayName("Deve lançar exceção quando to-do não for encontrado no updateById")
    void updateById_notFound_throwsException() {
        Long usuarioId = 1L;
        Long todoId = 10L;
        when(authentication.getName()).thenReturn(usuarioId.toString());
        when(repository.findByIdAndUsuarioId(todoId, usuarioId)).thenReturn(Optional.empty());

        todoUpdateDTO updates = new todoUpdateDTO("Título", null, null, null);

        assertThatThrownBy(() -> service.updateById(authentication, todoId, updates))
                .isInstanceOf(applicationException.class)
                .hasMessageContaining("To-do nao encontrado");

        verify(repository, never()).save(any());
    }

    // =========================================================
    // deleteById
    // =========================================================

    @Test
    @DisplayName("Deve deletar um to-do com sucesso")
    void deleteById_success() {
        Long usuarioId = 1L;
        Long todoId = 10L;
        when(authentication.getName()).thenReturn(usuarioId.toString());

        service.deleteById(authentication, todoId);

        verify(repository).deleteByTodoIdAndUsuarioId(todoId, usuarioId);
    }

    @Test
    @DisplayName("Deve chamar deleteByTodoIdAndUsuarioId com os IDs corretos")
    void deleteById_callsRepositoryWithCorrectIds() {
        Long usuarioId = 99L;
        Long todoId = 55L;
        when(authentication.getName()).thenReturn(usuarioId.toString());

        service.deleteById(authentication, todoId);

        verify(repository, times(1)).deleteByTodoIdAndUsuarioId(todoId, usuarioId);
        verifyNoMoreInteractions(repository);
    }

    // =========================================================
    // Helpers
    // =========================================================

    private List<todoListEntity> buildEntityList() {
        List<todoListEntity> list = new ArrayList<>();
        list.add(buildEntity(10L));
        return list;
    }

    private todoListEntity buildEntity(Long id) {
        todoListEntity entity = new todoListEntity();
        entity.setId(id);
        entity.setTitulo("Título");
        entity.setDescricao("Descrição");
        entity.setDtaValidade(LocalDate.now());
        entity.setStatus(StatusTodo.PENDENTE);
        return entity;
    }
}