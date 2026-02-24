package alexandreS.To_Do_List_API.service;

import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class usuarioService {
    @Autowired
    private usuarioRepository repository;

    public usuarioEntity saveUsuario(usuarioEntity usuario){
        return  repository.save(usuario);
    }

    public List<usuarioEntity> listAll(){
            return  repository.findAll();
    }

}
