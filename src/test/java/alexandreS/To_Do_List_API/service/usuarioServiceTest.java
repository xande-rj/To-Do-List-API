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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class usuarioServiceTest {
    @Mock
    private usuarioRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private jwtService jwtService;

    @Autowired
    @InjectMocks
    private usuarioService service;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retorna um erro pois o usuario ja existe nao banco")
    void saveUsuarioFail() {
        usuarioEntity data = new usuarioEntity(1L,"teste@gmail.com","testenome","senha123",new ArrayList<>());
        when(repository.save(data)).thenReturn(data);
        when(repository.existsByEmailUsuario(data.getEmailUsuario())).thenReturn(true);
        usuarioCadastroDTO userDTO = new usuarioCadastroDTO("teste@gmail.com","testenome","senha123");
        applicationException exception = Assertions.assertThrows(applicationException.class,()->{
            service.saveUsuario(userDTO);
        });

        Assertions.assertEquals("Email já cadastrado",exception.getMessage());

        verify(passwordEncoder, never()).encode(any());
    }


    @Test
    @DisplayName("Deve criar um usuario no banco de dados sem problema e retornar token")
    void saveUsuarioSucesses() {
        usuarioCadastroDTO userDTO = new usuarioCadastroDTO("teste@gmail.com","testenome","senha123");

        when(repository.existsByEmailUsuario(userDTO.getEmailUsuario())).thenReturn(false);
        when(passwordEncoder.encode(userDTO.getSenhaUsuario())).thenReturn("senhaCodificada");

        usuarioEntity entidadeSalva = new usuarioEntity();
        entidadeSalva.setId(1L);
        entidadeSalva.setEmailUsuario(userDTO.getEmailUsuario());
        entidadeSalva.setNomeUsuario(userDTO.getNomeUsuario());
        entidadeSalva.setSenhaUsuario("senhaCodificada");

        when(repository.save(any(usuarioEntity.class))).thenReturn(entidadeSalva);
        when(jwtService.gerarToken(1L)).thenReturn("tokenJWT");

        usuarioRetornoDTO resultado = service.saveUsuario(userDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getToken()).isEqualTo("tokenJWT");

        verify(repository).existsByEmailUsuario(userDTO.getEmailUsuario());
        verify(passwordEncoder).encode(userDTO.getSenhaUsuario());

    }

    @Test
    @DisplayName("Deve retornar token sem problemas")
    void loginUsuarioSucesses() {
        usuarioLoginDTO userDTO = new usuarioLoginDTO("teste@gmail.com","senha123");

        when(repository.existsByEmailUsuario(userDTO.getEmailUsuario())).thenReturn(true);

        usuarioEntity entidadeSalva = new usuarioEntity();
        entidadeSalva.setId(1L);
        entidadeSalva.setEmailUsuario(userDTO.getEmailUsuario());
        entidadeSalva.setSenhaUsuario("senhaCodificada");

        when(repository.findByEmailUsuario(entidadeSalva.getEmailUsuario())).thenReturn(Optional.of(entidadeSalva));
        when(passwordEncoder.matches(userDTO.getSenhaUsuario(),entidadeSalva.getSenhaUsuario())).thenReturn(true);

        when(jwtService.gerarToken(1L)).thenReturn("tokenJWT");

        usuarioRetornoDTO resultado = service.loginUsuario(userDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getToken()).isEqualTo("tokenJWT");

        verify(repository).findByEmailUsuario(userDTO.getEmailUsuario());
        verify(passwordEncoder).matches(userDTO.getSenhaUsuario(),entidadeSalva.getSenhaUsuario());
        verify(jwtService).gerarToken(1L);
    }

    @Test
    @DisplayName("Deve retornar erro pois o email nao existe")
    void loginUsuarioFail() {
        usuarioLoginDTO userDTO = new usuarioLoginDTO("teste@gmail.com","senha123");

        when(repository.existsByEmailUsuario(userDTO.getEmailUsuario())).thenReturn(false);


        applicationException exception = Assertions.assertThrows(applicationException.class,()->{
            service.loginUsuario(userDTO);
        });

        Assertions.assertEquals("Email não cadastrado",exception.getMessage());

        verify(passwordEncoder, never()).encode(any());
        verify(repository, never()).findByEmailUsuario(any());

    }

    @Test
    @DisplayName("Deve retornar erro pois a senha esta errada")
    void loginUsuarioFailCase02() {
        usuarioLoginDTO userDTO = new usuarioLoginDTO("teste@gmail.com","senha123");

        when(repository.existsByEmailUsuario(userDTO.getEmailUsuario())).thenReturn(true);

        usuarioEntity entidadeSalva = new usuarioEntity();
        entidadeSalva.setId(1L);
        entidadeSalva.setEmailUsuario(userDTO.getEmailUsuario());
        entidadeSalva.setSenhaUsuario("senhaCodificada");

        when(repository.findByEmailUsuario(entidadeSalva.getEmailUsuario())).thenReturn(Optional.of(entidadeSalva));
        when(passwordEncoder.matches(userDTO.getSenhaUsuario(),entidadeSalva.getSenhaUsuario())).thenReturn(false);


        applicationException exception = Assertions.assertThrows(applicationException.class,()->{
            service.loginUsuario(userDTO);
        });

        Assertions.assertEquals("Senha incorreta",exception.getMessage());


        verify(jwtService, never()).gerarToken(any());

    }
}