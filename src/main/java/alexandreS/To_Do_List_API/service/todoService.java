package alexandreS.To_Do_List_API.service;

import alexandreS.To_Do_List_API.DTO.todoDTO;
import alexandreS.To_Do_List_API.Enus.StatusTodo;
import alexandreS.To_Do_List_API.entitys.todoListEntity;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.errors.applicationException;
import alexandreS.To_Do_List_API.repository.todoRepository;
import alexandreS.To_Do_List_API.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class todoService {
    @Autowired
    private todoRepository repository;
    @Autowired
    private usuarioRepository userRepository;

    public todoListEntity saveList(todoDTO todo,Authentication authentication){

        Optional<usuarioEntity> user = userRepository.findById(Long.parseLong(authentication.getName()));
        if(user.isEmpty()){
            throw new applicationException(
                    "Usuario nao encontrado",
                    HttpStatus.NOT_FOUND
            );
        }

        todoListEntity todoList = new todoListEntity();
        todoList.setTitulo(todo.getTitulo());
        todoList.setDescricao(todo.getDescricao());
        todoList.setDtaValidade(todo.getDtaValidade());
        todoList.setStatus(todo.getStatus());
        todoList.setUsuario(user.get());

        return repository.save(todoList);
    }

    public List<todoDTO> listAll(StatusTodo status, LocalDate data, Authentication authentication){

        if(status !=null){
            List<todoListEntity> listEntities = repository.findByUsuarioIdAndStatus(Long.parseLong(authentication.getName()),status);
            return listEntities.stream().map(todo -> new todoDTO(
                    todo.getId(),
                    todo.getTitulo(),
                    todo.getDescricao(),
                    todo.getDtaValidade(),
                    todo.getStatus())).toList();
        }
        if(data!=null){
            List<todoListEntity> listEntities = repository.findByUsuarioIdAndDtaValidadeLessThanEqualOrderByDtaValidadeAsc(Long.parseLong(authentication.getName()),data);
            return listEntities.stream().map(todo -> new todoDTO(
                    todo.getId(),
                    todo.getTitulo(),
                    todo.getDescricao(),
                    todo.getDtaValidade(),
                    todo.getStatus())).toList();
        }
        List<todoListEntity> listEntities = repository.findByUsuarioId(Long.parseLong(authentication.getName()));
        if(listEntities.isEmpty()){
            throw new applicationException(
                    "Usuario nao encontrado",
                    HttpStatus.NOT_FOUND
            );
        }


        return listEntities.stream().map(todo -> new todoDTO(
                todo.getId(),
                todo.getTitulo(),
                todo.getDescricao(),
                todo.getDtaValidade(),
                todo.getStatus())).toList();
    }

    public  todoDTO getById(Authentication authentication,Long id){
       Optional<todoListEntity>  todoBd = repository.findByIdAndUsuarioId(id,Long.parseLong(authentication.getName()));
       if(todoBd.isEmpty()){
           throw new applicationException(
                   "To-do nao encontrado",
                   HttpStatus.NOT_FOUND
           );
       }

todoListEntity todo = todoBd.get();
        return new todoDTO(
                todo.getId(),
                todo.getTitulo(),
                todo.getDescricao(),
                todo.getDtaValidade(),
                todo.getStatus());

    }
}
