package escom.ipn.mx.ecoems.domain.repository;

import escom.ipn.mx.ecoems.domain.entity.Subtema;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubtemaRepository extends JpaRepository<Subtema, Long> {
    // Obtener subtemas ordenados por su campo 'orden'
    List<Subtema> findByTema_IdTemaOrderByOrdenAsc(Long idTema);
}