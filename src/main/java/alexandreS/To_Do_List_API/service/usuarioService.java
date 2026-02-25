package alexandreS.To_Do_List_API.service;

import alexandreS.To_Do_List_API.DTO.usuarioCadastroDTO;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class usuarioService {
    @Autowired
    private usuarioRepository repository;

    private final PasswordEncoder passwordEncoder;
    private final jwtService jwtService;

    public  usuarioService(PasswordEncoder passwordEncoder,jwtService jwtService ){
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    //Manter
    public String saveUsuario(usuarioCadastroDTO usuario){
        if(repository.existsByEmailUsuario(usuario.getEmailUsuario())){
            throw new RuntimeException("Usuário já existe");
        }


          usuarioEntity usuarioDb = new usuarioEntity();
            usuarioDb.setEmailUsuario(usuario.getEmailUsuario());
            usuarioDb.setNomeUsuario(usuario.getNomeUsuario());
            usuarioDb.setTodoList(usuario.getTodoList());
            String encryptedPassword = passwordEncoder.encode(usuario.getSenhaUsuario());
            usuarioDb.setSenhaUsuario(encryptedPassword);

            usuarioEntity usario = repository.save(usuarioDb);
        return jwtService.gerarToken(usario.getId(),usario.getEmailUsuario(),usario.getNomeUsuario());
    }

    //dev
    public List<usuarioEntity> listAll(){
        return  repository.findAll();
    }

}
