package escom.ipn.mx.ecoems.domain.repository;

import escom.ipn.mx.ecoems.domain.entity.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    // SELECT * FROM pregunta WHERE id_examen = ?
    List<Pregunta> findByExamen_IdExamen(Long idExamen);
}