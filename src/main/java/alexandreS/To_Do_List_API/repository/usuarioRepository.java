package alexandreS.To_Do_List_API.repository;

import alexandreS.To_Do_List_API.entitys.usuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface usuarioRepository extends JpaRepository<usuarioEntity,Long> {
    boolean existsByEmailUsuario(String emailUsuario);

Optional<usuarioEntity> findByEmailUsuario(String emailUsuario);
}
