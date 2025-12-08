package escom.ipn.mx.ecoems.api.controller;

import escom.ipn.mx.ecoems.domain.entity.Asignatura;
import escom.ipn.mx.ecoems.domain.entity.Tema;
import escom.ipn.mx.ecoems.domain.repository.AsignaturaRepository;
import escom.ipn.mx.ecoems.domain.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/temas")
@CrossOrigin(origins = "*")
public class TemaController {

    @Autowired
    private TemaRepository temaRepo;

    @Autowired
    private AsignaturaRepository asignaturaRepo;

    // Crear un tema vinculado a una asignatura
    // JSON esperado: { "nombre": "Álgebra", "contenido": "...", "asignatura": { "idAsignatura": 1 } }
    @PostMapping
    public ResponseEntity<?> crearTema(@RequestBody Tema tema) {
        // Validamos que venga la asignatura
        if (tema.getAsignatura() == null || tema.getAsignatura().getIdAsignatura() == null) {
            return new ResponseEntity<>("Error: Debes especificar el ID de la asignatura", HttpStatus.BAD_REQUEST);
        }

        // Verificamos que la asignatura exista
        Optional<Asignatura> asignaturaExiste = asignaturaRepo.findById(tema.getAsignatura().getIdAsignatura());
        if (asignaturaExiste.isEmpty()) {
            return new ResponseEntity<>("Error: La asignatura no existe", HttpStatus.NOT_FOUND);
        }

        tema.setAsignatura(asignaturaExiste.get());
        Tema nuevoTema = temaRepo.save(tema);
        return new ResponseEntity<>(nuevoTema, HttpStatus.CREATED);
    }

    // ---------------------------------------------------
    // NUEVOS MÉTODOS: ACTUALIZAR Y ELIMINAR
    // ---------------------------------------------------

    // 2. ACTUALIZAR UN TEMA (Ej: Corregir ortografía o ampliar contenido)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTema(@PathVariable Long id, @RequestBody Tema datosNuevos) {
        return temaRepo.findById(id).map(tema -> {
            tema.setNombre(datosNuevos.getNombre());
            tema.setContenido(datosNuevos.getContenido());
            // Nota: No cambiamos la asignatura aquí para mantenerlo simple,
            // solo actualizamos la info del tema.
            return new ResponseEntity<>(temaRepo.save(tema), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 3. ELIMINAR UN TEMA
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTema(@PathVariable Long id) {
        if (!temaRepo.existsById(id)) {
            return new ResponseEntity<>("El tema no existe", HttpStatus.NOT_FOUND);
        }
        try {
            temaRepo.deleteById(id);
            return new ResponseEntity<>("Tema eliminado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar (posiblemente esté en uso): " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}