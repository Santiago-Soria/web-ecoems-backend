package escom.ipn.mx.ecoems.domain.repository;

import escom.ipn.mx.ecoems.domain.entity.Tema;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TemaRepository extends JpaRepository<Tema, Long> {
    // Buscar todos los temas que pertenezcan a una Asignatura específica
    List<Tema> findByAsignatura_IdAsignatura(Long idAsignatura);
}