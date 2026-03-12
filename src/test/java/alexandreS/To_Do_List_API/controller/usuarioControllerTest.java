package alexandreS.To_Do_List_API.controller;

import alexandreS.To_Do_List_API.DTOS.usuarioCadastroDTO;
import alexandreS.To_Do_List_API.DTOS.usuarioLoginDTO;
import alexandreS.To_Do_List_API.DTOS.usuarioRetornoDTO;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.errors.applicationException;
import alexandreS.To_Do_List_API.repository.usuarioRepository;
import alexandreS.To_Do_List_API.service.jwtService;
import alexandreS.To_Do_List_API.service.usuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(usuarioController.class)
class usuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private jwtService jwtService;

    @MockitoBean
    private usuarioService service;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("POST /usuario - deve criar usuário com sucesso")
    void criarUsuarioCaso01() throws Exception {
        usuarioCadastroDTO dto = new usuarioCadastroDTO("email@teste.com", "Nome", "senha123");
        usuarioRetornoDTO responseDTO = new usuarioRetornoDTO("tokenJWT");
        when(service.saveUsuario(any(usuarioCadastroDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/usuario").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /usuario - retornar erro pois o email ja existe")
    void criarUsuarioCaso02() throws Exception {
        usuarioCadastroDTO dto = new usuarioCadastroDTO("email@teste.com", "Nome", "senha123");
        usuarioRetornoDTO responseDTO = new usuarioRetornoDTO("tokenJWT");
        when(service.saveUsuario(any(usuarioCadastroDTO.class))).thenThrow(new applicationException("Email já cadastrado", HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/usuario").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Email já cadastrado"));
    }

    @Test
    @DisplayName("POST /usuario/login - deve retorna o token")
    void LoginUsuarioCase01() throws Exception {
        usuarioLoginDTO dto = new usuarioLoginDTO("email@teste.com",  "senha123");
        usuarioRetornoDTO responseDTO = new usuarioRetornoDTO("tokenJWT");
        when(service.loginUsuario(any(usuarioLoginDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/usuario/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /usuario/login - deve retorna erro pois email nao existe")
    void LoginUsuarioCase02() throws Exception {
        usuarioLoginDTO dto = new usuarioLoginDTO("email@teste.com",  "senha123");


        when(service.loginUsuario(any(usuarioLoginDTO.class))).thenThrow(new applicationException("Email não cadastrado", HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/usuario/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Email não cadastrado"));
    }

    @Test
    @DisplayName("POST /usuario/login - deve retorna erro pois o senha esta errado")
    void LoginUsuarioCase03() throws Exception {
        usuarioLoginDTO dto = new usuarioLoginDTO("email@teste.com",  "senha123");


        when(service.loginUsuario(any(usuarioLoginDTO.class))).thenThrow(new applicationException("Senha incorreta", HttpStatus.NOT_ACCEPTABLE));

        mockMvc.perform(post("/usuario/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotAcceptable()).andExpect(jsonPath("$.message").value("Senha incorreta"));
    }

}