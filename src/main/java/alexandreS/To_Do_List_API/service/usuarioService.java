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

    public usuarioDTO saveUsuario(usuarioEntity usuario){
          usuarioEntity user =repository.save(usuario);
          usuarioDTO userDTO = new usuarioDTO(user.getEmailUsuario(),user.getNomeUsuario());
          return  userDTO;
    }

    public List<usuarioEntity> listAll(){
        return  repository.findAll();
    }

}
