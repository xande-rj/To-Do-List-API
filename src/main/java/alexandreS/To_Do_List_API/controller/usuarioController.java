package alexandreS.To_Do_List_API.controller;


import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.service.usuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class usuarioController {
    @Autowired
    private  usuarioService service;

    @PostMapping("")
    public  usuarioEntity saveUsuario(usuarioEntity usuario){
        return  service.saveUsuario(usuario);
    }
    @GetMapping("/all")
    public ResponseEntity<List<usuarioEntity>> findAll(){

        return  new ResponseEntity<>(service.listAll(),HttpStatus.OK);
    }



}
