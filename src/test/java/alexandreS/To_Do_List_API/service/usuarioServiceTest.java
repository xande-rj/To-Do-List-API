package alexandreS.To_Do_List_API.service;

import alexandreS.To_Do_List_API.DTOS.usuarioCadastroDTO;
import alexandreS.To_Do_List_API.DTOS.usuarioLoginDTO;
import alexandreS.To_Do_List_API.DTOS.usuarioRetornoDTO;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.errors.applicationException;
import alexandreS.To_Do_List_API.repository.usuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class usuarioServiceTest {

    @Mock
    private usuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private jwtService jwtService;

    private usuarioService service;

    @BeforeEach
    void setUp() {
        this.service = new usuarioService(repository, passwordEncoder, jwtService);
    }

    @Test
    @DisplayName("Deve retorna um erro pois o usuario ja existe nao banco")
    void saveUsuarioCase01() {
        usuarioCadastroDTO dto = new usuarioCadastroDTO("teste@gmail.com", "nome", "senha123");
        when(repository.existsByEmailUsuario(dto.getEmailUsuario())).thenReturn(true);

        assertThatThrownBy(() -> service.saveUsuario(dto))
                .isInstanceOf(applicationException.class)
                .hasMessageContaining("Email já cadastrado");

        verify(repository).existsByEmailUsuario(dto.getEmailUsuario());
    }


    @Test
    @DisplayName("Deve criar um usuario no banco de dados sem problema e retornar token")
    void saveUsuarioCase02() {
        usuarioCadastroDTO userDTO = new usuarioCadastroDTO("teste@gmail.com","testenome","senha123");

        when(this.repository.existsByEmailUsuario(userDTO.getEmailUsuario())).thenReturn(false);
        when(this.passwordEncoder.encode(userDTO.getSenhaUsuario())).thenReturn("senhaCodificada");

        usuarioEntity entidadeSalva = new usuarioEntity();
        entidadeSalva.setId(1L);
        entidadeSalva.setEmailUsuario(userDTO.getEmailUsuario());
        entidadeSalva.setNomeUsuario(userDTO.getNomeUsuario());
        entidadeSalva.setSenhaUsuario("senhaCodificada");

        when(this.repository.save(any(usuarioEntity.class))).thenReturn(entidadeSalva);
        when(this.jwtService.gerarToken(1L)).thenReturn("tokenJWT");

        usuarioRetornoDTO resultado = this.service.saveUsuario(userDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getToken()).isEqualTo("tokenJWT");

        verify(this.repository).existsByEmailUsuario(userDTO.getEmailUsuario());
        verify(this.passwordEncoder).encode(userDTO.getSenhaUsuario());

    }

    @Test
    @DisplayName("Deve retornar token sem problemas")
    void loginUsuarioCase01() {
        usuarioLoginDTO userDTO = new usuarioLoginDTO("teste@gmail.com","senha123");

        when(this.repository.existsByEmailUsuario(userDTO.getEmailUsuario())).thenReturn(true);

        usuarioEntity entidadeSalva = new usuarioEntity();
        entidadeSalva.setId(1L);
        entidadeSalva.setEmailUsuario(userDTO.getEmailUsuario());
        entidadeSalva.setSenhaUsuario("senhaCodificada");

        when(this.repository.findByEmailUsuario(entidadeSalva.getEmailUsuario())).thenReturn(Optional.of(entidadeSalva));
        when(this.passwordEncoder.matches(userDTO.getSenhaUsuario(),entidadeSalva.getSenhaUsuario())).thenReturn(true);

        when(this.jwtService.gerarToken(1L)).thenReturn("tokenJWT");

        usuarioRetornoDTO resultado = this.service.loginUsuario(userDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getToken()).isEqualTo("tokenJWT");

        verify(this.repository).findByEmailUsuario(userDTO.getEmailUsuario());
        verify(this.passwordEncoder).matches(userDTO.getSenhaUsuario(),entidadeSalva.getSenhaUsuario());
        verify(this.jwtService).gerarToken(1L);
    }

    @Test
    @DisplayName("Deve retornar erro pois o email nao existe")
    void loginUsuarioCase02() {
        usuarioLoginDTO userDTO = new usuarioLoginDTO("teste@gmail.com","senha123");

        when(this.repository.existsByEmailUsuario(userDTO.getEmailUsuario())).thenReturn(false);


        applicationException exception = Assertions.assertThrows(applicationException.class,()->{
            this.service.loginUsuario(userDTO);
        });

        Assertions.assertEquals("Email não cadastrado",exception.getMessage());

        verify(this.passwordEncoder, never()).encode(any());
        verify(this.repository, never()).findByEmailUsuario(any());

    }

    @Test
    @DisplayName("Deve retornar erro pois a senha esta errada")
    void loginUsuarioCase03() {
        usuarioLoginDTO userDTO = new usuarioLoginDTO("teste@gmail.com","senha123");

        when(this.repository.existsByEmailUsuario(userDTO.getEmailUsuario())).thenReturn(true);

        usuarioEntity entidadeSalva = new usuarioEntity();
        entidadeSalva.setId(1L);
        entidadeSalva.setEmailUsuario(userDTO.getEmailUsuario());
        entidadeSalva.setSenhaUsuario("senhaCodificada");

        when(this.repository.findByEmailUsuario(entidadeSalva.getEmailUsuario())).thenReturn(Optional.of(entidadeSalva));
        when(this.passwordEncoder.matches(userDTO.getSenhaUsuario(),entidadeSalva.getSenhaUsuario())).thenReturn(false);


        applicationException exception = Assertions.assertThrows(applicationException.class,()->{
            this.service.loginUsuario(userDTO);
        });

        Assertions.assertEquals("Senha incorreta",exception.getMessage());


        verify(this.jwtService, never()).gerarToken(any());

    }
}