package cl.usach.toolrent.repositories;
import cl.usach.toolrent.entities.KardexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


//Esta creado pero no se ocupa porque gracias al jpa puedes hacer las consultas bien
@Repository
public interface KardexRepository extends JpaRepository<KardexEntity, Long> {

}
