package alexandreS.To_Do_List_API.controller;

import alexandreS.To_Do_List_API.DTOS.todoUpdateDTO;
import alexandreS.To_Do_List_API.DTOS.todoRetornoDTO;
import alexandreS.To_Do_List_API.DTOS.todoSaveDTO;
import alexandreS.To_Do_List_API.Enus.StatusTodo;
import alexandreS.To_Do_List_API.entitys.todoListEntity;
import alexandreS.To_Do_List_API.service.todoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/todo")
public class todoController {
    @Autowired
    private todoService service;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/save")
    @Operation(summary = "Criar um novo To-do")
    public ResponseEntity<todoRetornoDTO> save(@RequestBody @Valid todoSaveDTO todo, Authentication authentication){
        todoListEntity entity = service.saveList(todo,authentication);

        return new ResponseEntity<>(new todoRetornoDTO(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getDtaValidade(),
                entity.getStatus()
        ), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/all")
    @Operation(summary = "Retorna uma lista de To-do")
    public ResponseEntity<List<todoRetornoDTO>> getAll(@RequestParam(required = false) StatusTodo status,
                                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                                       Authentication authentication){
    return new ResponseEntity<>(service.listAll(status,data,authentication), HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    @Operation(summary = "Retorna um To-do")
    public ResponseEntity<todoRetornoDTO> getById(Authentication authentication, @PathVariable Long id){
        return new ResponseEntity<>(service.getById(authentication,id), HttpStatus.OK);
    }
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza o To-do com os campos que deseja")
    public ResponseEntity<todoRetornoDTO> updateById(Authentication authentication, @PathVariable Long id, @RequestBody todoUpdateDTO update){
         todoListEntity entity =service.updateById(authentication,id,update);
        return new ResponseEntity<>(new todoRetornoDTO(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getDtaValidade(),
                entity.getStatus()
        ), HttpStatus.CREATED);
    }
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta o To-do pelo ID")
    public  ResponseEntity<Void> deleteById(Authentication authentication,@PathVariable Long id){
         service.deleteById(authentication,id);
         return  new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
