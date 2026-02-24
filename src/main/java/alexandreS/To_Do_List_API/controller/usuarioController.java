package alexandreS.To_Do_List_API.controller;


import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.service.usuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class usuarioController {
    @Autowired
    private  usuarioService service;

    @PostMapping()
    public  ResponseEntity<usuarioEntity>saveUsuario( @RequestBody usuarioEntity usuario){
        return new ResponseEntity<>(service.saveUsuario(usuario),HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<List<usuarioEntity>> findAll(){

        return  new ResponseEntity<>(service.listAll(),HttpStatus.OK);
    }



}
