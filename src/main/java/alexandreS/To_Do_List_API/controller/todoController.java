package alexandreS.To_Do_List_API.controller;

import alexandreS.To_Do_List_API.DTO.todoDTO;
import alexandreS.To_Do_List_API.Enus.StatusTodo;
import alexandreS.To_Do_List_API.entitys.todoListEntity;
import alexandreS.To_Do_List_API.service.todoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/todo")
public class todoController {
    @Autowired
    private todoService service;

    @PostMapping("/save")
    public ResponseEntity<todoDTO> save(@RequestBody @Valid todoDTO todo, Authentication authentication){
        todoListEntity entity = service.saveList(todo,authentication);

        return new ResponseEntity<>(new todoDTO(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getDtaValidade(),
                entity.getStatus()
        ), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<todoDTO>> getAll(@RequestParam(required = false) StatusTodo status, Authentication authentication){
    return new ResponseEntity<>(service.listAll(status,authentication), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<todoDTO> getById(Authentication authentication,@PathVariable Long id){
        return new ResponseEntity<>(service.getById(authentication,id), HttpStatus.OK);
    }

}
