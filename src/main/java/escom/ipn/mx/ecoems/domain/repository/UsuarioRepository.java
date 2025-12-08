package escom.ipn.mx.ecoems.domain.repository;

import escom.ipn.mx.ecoems.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método extra para buscar usuario por correo (útil para login)
    Optional<Usuario> findByCorreo(String correo);
}