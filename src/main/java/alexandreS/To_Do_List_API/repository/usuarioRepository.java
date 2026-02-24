package alexandreS.To_Do_List_API.repository;

import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface usuarioRepository extends JpaRepository<usuarioEntity,String> {

}
