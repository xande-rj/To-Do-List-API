package alexandreS.To_Do_List_API.repository;

import alexandreS.To_Do_List_API.entitys.todoListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface todoRepository extends JpaRepository<todoListEntity,Long> {
    List<todoListEntity> findByUsuarioId(Long id);

    Optional<todoListEntity> findByIdAndUsuarioId(Long todoId, Long usuarioId);
}
