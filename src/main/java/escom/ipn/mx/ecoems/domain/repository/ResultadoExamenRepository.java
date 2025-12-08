package escom.ipn.mx.ecoems.domain.repository;

import escom.ipn.mx.ecoems.domain.entity.ResultadoExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultadoExamenRepository extends JpaRepository<ResultadoExamen, Long> {
    // Buscar todos los exámenes resueltos por un usuario
    List<ResultadoExamen> findByUsuario_IdUsuario(Long idUsuario);
}