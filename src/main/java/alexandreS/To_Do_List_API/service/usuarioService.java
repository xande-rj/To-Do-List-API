package alexandreS.To_Do_List_API.service;

import alexandreS.To_Do_List_API.DTO.usuarioDTO;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class usuarioService {
    @Autowired
    private usuarioRepository repository;

    //Manter
    public usuarioDTO saveUsuario(usuarioEntity usuario){
        if(repository.existsByEmailUsuario(usuario.getEmailUsuario())){
            throw new RuntimeException("Usuário já existe");
        }
          usuarioEntity user =repository.save(usuario);

          return  new usuarioDTO(user.getEmailUsuario(),user.getNomeUsuario(),user.getTodoList());
    }

    //dev
    public List<usuarioEntity> listAll(){
        return  repository.findAll();
    }

}
