package alexandreS.To_Do_List_API.service;

import alexandreS.To_Do_List_API.DTO.usuarioDTO;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class usuarioService {
    @Autowired
    private usuarioRepository repository;

    private final PasswordEncoder passwordEncoder;

    public  usuarioService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    //Manter
    public String saveUsuario(usuarioDTO usuario){
        if(repository.existsByEmailUsuario(usuario.getEmailUsuario())){
            throw new RuntimeException("Usuário já existe");
        }


          usuarioEntity usuarioDb = new usuarioEntity();
            usuarioDb.setEmailUsuario(usuario.getEmailUsuario());
            usuarioDb.setNomeUsuario(usuario.getNomeUsuario());
            usuarioDb.setTodoList(usuario.getTodoList());
            String encryptedPassword = passwordEncoder.encode(usuario.getSenhaUsuario());
            usuarioDb.setSenhaUsuario(encryptedPassword);

            repository.save(usuarioDb);

          return  "";
    }

    //dev
    public List<usuarioEntity> listAll(){
        return  repository.findAll();
    }

}
