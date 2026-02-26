package alexandreS.To_Do_List_API.service;

import alexandreS.To_Do_List_API.DTO.todoDTO;
import alexandreS.To_Do_List_API.entitys.todoListEntity;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.repository.todoRepository;
import alexandreS.To_Do_List_API.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class todoService {
    @Autowired
    private todoRepository repository;
    @Autowired
    private usuarioRepository userRepository;

    public todoListEntity saveList(todoDTO todo,Authentication authentication){


        usuarioEntity user = userRepository.findById(Long.parseLong(authentication.getName())).get();

        todoListEntity todoList = new todoListEntity();
        todoList.setTitulo(todo.getTitulo());
        todoList.setDescricao(todo.getDescricao());
        todoList.setDtaValidade(todo.getDtaValidade());
        todoList.setStatus(todo.getStatus());
        todoList.setUsuario(user);
        return repository.save(todoList);


    }

    public List<todoDTO> listAll(Authentication authentication){

        return repository.findByUsuarioId(Long.parseLong(authentication.getName())).stream().map(todo -> new todoDTO(
                todo.getTitulo(),
                todo.getDescricao(),
                todo.getDtaValidade(),
                todo.getStatus())).toList();
    }
}
