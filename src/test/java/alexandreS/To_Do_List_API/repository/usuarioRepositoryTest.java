package alexandreS.To_Do_List_API.repository;

import alexandreS.To_Do_List_API.DTOS.usuarioCadastroDTO;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class usuarioRepositoryTest {
    @Autowired
    usuarioRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Usario ja existe no banco de dados retorna TRUE")
    void existsByEmailUsuarioCase01() {
        usuarioCadastroDTO data = new usuarioCadastroDTO("teste@gmail.com","nomeTeste","testeSenha123");
        this.createUser(data);
boolean result = this.repository.existsByEmailUsuario(data.getEmailUsuario());

assertThat(result).isTrue();

    }

    @Test
    @DisplayName("Usario nao existe no banco de dados retorna FALSE")
    void existsByEmailUsuarioCase02() {
        usuarioCadastroDTO data = new usuarioCadastroDTO("teste@gmail.com","nomeTeste","testeSenha123");

        boolean result = this.repository.existsByEmailUsuario(data.getEmailUsuario());

        assertThat(result).isFalse();

    }

    @Test
    @DisplayName("Usuario no banco existe")
    void findByEmailUsuarioCase01() {
        usuarioCadastroDTO data = new usuarioCadastroDTO("teste@gmail.com","nomeTeste","testeSenha123");
        this.createUser(data);
        Optional<usuarioEntity> result = this.repository.findByEmailUsuario(data.getEmailUsuario());

        assertThat(result.isPresent()).isTrue();

    }

    @Test
    @DisplayName("Usuario nao banco nao existe")
    void findByEmailUsuarioCase02() {
        usuarioCadastroDTO data = new usuarioCadastroDTO("teste@gmail.com","nomeTeste","testeSenha123");

        Optional<usuarioEntity> result = this.repository.findByEmailUsuario(data.getEmailUsuario());

        assertThat(result.isEmpty()).isTrue();

    }

    private usuarioEntity createUser(usuarioCadastroDTO data){
        usuarioEntity user = new usuarioEntity();
        user.setEmailUsuario(data.getEmailUsuario());
        user.setNomeUsuario(data.getNomeUsuario());
        user.setSenhaUsuario(data.getSenhaUsuario());
        this.entityManager.persist(user);
        return user;
    }

}