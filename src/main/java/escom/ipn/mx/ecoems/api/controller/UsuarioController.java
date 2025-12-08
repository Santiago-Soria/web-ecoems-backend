package escom.ipn.mx.ecoems.api.controller;

import escom.ipn.mx.ecoems.domain.entity.Usuario;
import escom.ipn.mx.ecoems.domain.repository.UsuarioRepository;
import escom.ipn.mx.ecoems.domain.service.CorreoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "*") // Permite que React se conecte después
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CorreoService correoService; // <--- Agrega esto

    // 1. Obtener todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // 2. Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioRepository.save(usuario);

            // --- ENVIAR CORREO DE BIENVENIDA ---
            String asunto = "¡Bienvenido a ECOEMS!";
            String cuerpo = "<h1>Hola " + nuevoUsuario.getNombre() + "</h1>" +
                    "<p>Tu cuenta ha sido creada exitosamente en la plataforma.</p>" +
                    "<p>Tu usuario es: <b>" + nuevoUsuario.getCorreo() + "</b></p>";

            // Lo ponemos en un hilo aparte para que no alente la respuesta del servidor
            new Thread(() -> {
                correoService.enviarCorreoSimple(nuevoUsuario.getCorreo(), asunto, cuerpo);
            }).start();
            // ------------------------------------

            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3. ACTUALIZAR USUARIO
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDatos) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(usuarioDatos.getNombre());
            usuario.setApPaterno(usuarioDatos.getApPaterno());
            usuario.setApMaterno(usuarioDatos.getApMaterno());
            // Solo actualizamos contraseña si envían una nueva
            if (usuarioDatos.getContrasena() != null && !usuarioDatos.getContrasena().isEmpty()) {
                usuario.setContrasena(usuarioDatos.getContrasena());
            }
            return new ResponseEntity<>(usuarioRepository.save(usuario), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            if (!usuarioRepository.existsById(id)) {
                return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
            }
            usuarioRepository.deleteById(id);
            return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.OK);
        } catch (Exception e) {
            // Este error suele salir si el usuario ya tiene resultados de exámenes (Integridad referencial)
            return new ResponseEntity<>("No se puede eliminar: El usuario tiene registros asociados (exámenes realizados).", HttpStatus.CONFLICT);
        }
    }
}