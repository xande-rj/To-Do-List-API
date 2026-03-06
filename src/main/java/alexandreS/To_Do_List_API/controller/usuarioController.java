package alexandreS.To_Do_List_API.controller;


import alexandreS.To_Do_List_API.DTOS.usuarioCadastroDTO;
import alexandreS.To_Do_List_API.DTOS.usuarioLoginDTO;
import alexandreS.To_Do_List_API.DTOS.usuarioRetornoDTO;
import alexandreS.To_Do_List_API.service.usuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/usuario")
public class usuarioController {
    @Autowired
    private  usuarioService service;

    @PostMapping()
    @Operation(summary = "Criar um novo usuario")
    public ResponseEntity<usuarioRetornoDTO> saveUsuario(@RequestBody @Valid usuarioCadastroDTO usuario){
        return new ResponseEntity<>(service.saveUsuario(usuario),HttpStatus.CREATED);
    }
    @PostMapping("/login")
    @Operation(summary = "Login de um usuario")
    public  ResponseEntity<usuarioRetornoDTO> loginUsuario(@RequestBody @Valid usuarioLoginDTO usuario){
        return new ResponseEntity<>(service.loginUsuario(usuario),HttpStatus.OK);
    }




}
