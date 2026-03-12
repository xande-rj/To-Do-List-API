package alexandreS.To_Do_List_API.repository;

import alexandreS.To_Do_List_API.DTOS.todoSaveDTO;
import alexandreS.To_Do_List_API.DTOS.usuarioCadastroDTO;
import alexandreS.To_Do_List_API.Enus.StatusTodo;
import alexandreS.To_Do_List_API.entitys.todoListEntity;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class todoRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    todoRepository repository;

    @Test
    @DisplayName("Deve retorna List<todoListEntity> com base no id do usuario")

    void findByUsuarioIdCase01() {

        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        todoSaveDTO dataTodo = new todoSaveDTO("titulo Teste", "descricao", LocalDate.now(), StatusTodo.PENDENTE);
        this.createTodo(dataTodo, dataSaveUser);

        List<todoListEntity> result = this.repository.findByUsuarioId(dataSaveUser.getId());

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitulo()).isEqualTo(dataTodo.getTitulo());
    }

    @Test
    @DisplayName("Deve retorna Optional<todoListEntity> com base no id do usuario e do to-do")
    void findByUsuarioIdCase02() {

        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        todoSaveDTO dataTodo = new todoSaveDTO("titulo Teste", "descricao", LocalDate.now(), StatusTodo.PENDENTE);
        todoListEntity todoList = this.createTodo(dataTodo, dataSaveUser);

        Optional<todoListEntity> result = this.repository.findByIdAndUsuarioId(todoList.getId(), dataSaveUser.getId());

        assertThat(result.isEmpty()).isFalse();

    }

    @Test
    @DisplayName("Deve retorna List<todoListEntity> com base no id do usuario e do status")
    void findByUsuarioIdCase03() {

        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        todoSaveDTO dataTodo = new todoSaveDTO("titulo Teste", "descricao", LocalDate.now(), StatusTodo.PENDENTE);
        this.createTodo(dataTodo, dataSaveUser);

        List<todoListEntity> result = this.repository.findByUsuarioIdAndStatus(dataSaveUser.getId(), StatusTodo.PENDENTE);

        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Deve retorna List<todoListEntity> com base no id do usuario e na data de validade do to-do")
    void findByUsuarioIdCase04() {
        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        todoSaveDTO dataTodo = new todoSaveDTO("titulo Teste", "descricao", LocalDate.now(), StatusTodo.PENDENTE);
        this.createTodo(dataTodo, dataSaveUser);

        List<todoListEntity> result = this.repository.findByUsuarioIdAndDtaValidadeLessThanEqualOrderByDtaValidadeAsc(dataSaveUser.getId(), LocalDate.now());

        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Deve retorna List<todoListEntity> com base no id do usuario e do status e na data de validade do to-do")
    void findByUsuarioIdCase05() {
        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        todoSaveDTO dataTodo = new todoSaveDTO("titulo Teste", "descricao", LocalDate.now(), StatusTodo.PENDENTE);
        this.createTodo(dataTodo, dataSaveUser);

        List<todoListEntity> result = this.repository.findByUsuarioIdAndStatusAndDtaValidadeLessThanEqual(dataSaveUser.getId(), StatusTodo.PENDENTE, LocalDate.now());

        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Deve apaga todo pelo id do usuario e do to-do ")

    void findByUsuarioIdCase06() {

        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        todoSaveDTO dataTodo = new todoSaveDTO("titulo Teste", "descricao", LocalDate.now(), StatusTodo.PENDENTE);
        todoListEntity todoList = this.createTodo(dataTodo, dataSaveUser);

        this.repository.deleteByTodoIdAndUsuarioId(todoList.getId(), dataSaveUser.getId());
        List<todoListEntity> result = this.repository.findByUsuarioId(dataSaveUser.getId());

        assertThat(result.isEmpty()).isTrue();
    }

    // erros
    @Test
    @DisplayName("Deve retorna List<todoListEntity> vazio pois nao existe to-dos")
    void findByUsuarioIdCase07() {

        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        List<todoListEntity> result = this.repository.findByUsuarioId(dataSaveUser.getId());
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retorna Optional<todoListEntity> com base no id do to-do vazio pois nao existe pois nao existe to-do com esse id do usuario")

    void findByUsuarioIdCase08() {

        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        Optional<todoListEntity> result = this.repository.findByIdAndUsuarioId(1L, dataSaveUser.getId());

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retorna List<todoListEntity> vazio pois nao existe com status")
    void findByUsuarioIdCase09() {

        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        todoSaveDTO dataTodo = new todoSaveDTO("titulo Teste", "descricao", LocalDate.now(), StatusTodo.PENDENTE);
        this.createTodo(dataTodo, dataSaveUser);

        List<todoListEntity> result = this.repository.findByUsuarioIdAndStatus(dataSaveUser.getId(), StatusTodo.CONCLUIDO);

        assertThat(result.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Deve retorna List<todoListEntity> vazio pois nao ha data que foi passsada")
    void findByUsuarioIdCase10() {
        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        todoSaveDTO dataTodo = new todoSaveDTO("titulo Teste", "descricao", LocalDate.now(), StatusTodo.PENDENTE);
        this.createTodo(dataTodo, dataSaveUser);

        List<todoListEntity> result = this.repository.findByUsuarioIdAndDtaValidadeLessThanEqualOrderByDtaValidadeAsc(dataSaveUser.getId(), LocalDate.parse("2021-01-07"));

        assertThat(result.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Deve retorna List<todoListEntity> vazio pois nao ha data que foi passsada e nem status")

    void findByUsuarioIdCase11() {

        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        todoSaveDTO dataTodo = new todoSaveDTO("titulo Teste", "descricao", LocalDate.now(), StatusTodo.PENDENTE);
        this.createTodo(dataTodo, dataSaveUser);

        List<todoListEntity> result = this.repository.findByUsuarioIdAndStatusAndDtaValidadeLessThanEqual(dataSaveUser.getId(), StatusTodo.CONCLUIDO, LocalDate.now());

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retorna List<todoListEntity> pois nao foi o usuario que tem o to-do com id que apagou")
    void findByUsuarioIdCase12() {
        usuarioCadastroDTO dataUser = new usuarioCadastroDTO("teste@gmail.com", "nomeTeste", "testeSenha123");
        usuarioEntity dataSaveUser = this.createUser(dataUser);
        todoSaveDTO dataTodo = new todoSaveDTO("titulo Teste", "descricao", LocalDate.now(), StatusTodo.PENDENTE);
        todoListEntity todoList = this.createTodo(dataTodo, dataSaveUser);

        this.repository.deleteByTodoIdAndUsuarioId(todoList.getId(), 2L);
        List<todoListEntity> result = this.repository.findByUsuarioId(dataSaveUser.getId());

        assertThat(result.isEmpty()).isFalse();
    }

    private usuarioEntity createUser(usuarioCadastroDTO data) {
        usuarioEntity user = new usuarioEntity();
        user.setEmailUsuario(data.getEmailUsuario());
        user.setNomeUsuario(data.getNomeUsuario());
        user.setSenhaUsuario(data.getSenhaUsuario());
        this.entityManager.persist(user);
        return user;
    }


    private todoListEntity createTodo(todoSaveDTO data, usuarioEntity dataUser) {
        todoListEntity todo = new todoListEntity();
        todo.setTitulo(data.getTitulo());
        todo.setDescricao(data.getDescricao());
        todo.setDtaValidade(data.getDtaValidade());
        todo.setStatus(data.getStatus());
        todo.setUsuario(dataUser);
        this.entityManager.persist(todo);

        return todo;
    }
}