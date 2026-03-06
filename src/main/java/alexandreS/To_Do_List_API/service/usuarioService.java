package alexandreS.To_Do_List_API.service;

import alexandreS.To_Do_List_API.DTOS.usuarioCadastroDTO;
import alexandreS.To_Do_List_API.DTOS.usuarioLoginDTO;
import alexandreS.To_Do_List_API.DTOS.usuarioRetornoDTO;
import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import alexandreS.To_Do_List_API.errors.applicationException;
import alexandreS.To_Do_List_API.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class usuarioService {
    @Autowired
    private usuarioRepository repository;

    private final PasswordEncoder passwordEncoder;
    private final jwtService jwtService;

    public  usuarioService(PasswordEncoder passwordEncoder,jwtService jwtService ){
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public usuarioRetornoDTO saveUsuario(usuarioCadastroDTO usuario){
        if(repository.existsByEmailUsuario(usuario.getEmailUsuario())){
            throw new applicationException(
                    "Email já cadastrado",
                    HttpStatus.BAD_REQUEST
            );

        }
          usuarioEntity usuarioDb = new usuarioEntity();
            usuarioDb.setEmailUsuario(usuario.getEmailUsuario());
            usuarioDb.setNomeUsuario(usuario.getNomeUsuario());

            String encryptedPassword = passwordEncoder.encode(usuario.getSenhaUsuario());
            usuarioDb.setSenhaUsuario(encryptedPassword);

            usuarioEntity entity = repository.save(usuarioDb);

        usuarioRetornoDTO token = new usuarioRetornoDTO(jwtService.gerarToken(entity.getId()));
        return token;
    }

    public usuarioRetornoDTO loginUsuario(usuarioLoginDTO usuario) {
        if(!repository.existsByEmailUsuario(usuario.getEmailUsuario())){
            throw new applicationException(
                    "Email não cadastrado",
                    HttpStatus.BAD_REQUEST
            );
        }
        usuarioEntity usuariodb= repository.findByEmailUsuario(usuario.getEmailUsuario()).get();

        if(!passwordEncoder.matches(usuario.getSenhaUsuario(),usuariodb.getSenhaUsuario())){
            throw new applicationException(
                    "Senha incorreta",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        usuarioRetornoDTO token = new usuarioRetornoDTO(jwtService.gerarToken(usuariodb.getId()));
        return token;
    }


}
