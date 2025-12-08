package escom.ipn.mx.ecoems.api.controller;

import escom.ipn.mx.ecoems.domain.entity.Asignatura;
import escom.ipn.mx.ecoems.domain.entity.Tema;
import escom.ipn.mx.ecoems.domain.repository.AsignaturaRepository;
import escom.ipn.mx.ecoems.domain.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asignaturas")
@CrossOrigin(origins = "*")
public class AsignaturaController {

    @Autowired
    private AsignaturaRepository asignaturaRepo;

    @Autowired
    private TemaRepository temaRepo;

    // Obtener todas las asignaturas
    @GetMapping
    public List<Asignatura> listarAsignaturas() {
        return asignaturaRepo.findAll();
    }

    // Crear una asignatura (Ej: "Matemáticas")
    @PostMapping
    public ResponseEntity<Asignatura> crearAsignatura(@RequestBody Asignatura asignatura) {
        return new ResponseEntity<>(asignaturaRepo.save(asignatura), HttpStatus.CREATED);
    }

    // Obtener los temas de una asignatura (Ej: Temas de Matemáticas ID 1)
    @GetMapping("/{id}/temas")
    public List<Tema> verTemasDeAsignatura(@PathVariable Long id) {
        return temaRepo.findByAsignatura_IdAsignatura(id);
    }

    // ACTUALIZAR ASIGNATURA (Ej: Corregir "Matematicas" a "Matemáticas")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAsignatura(@PathVariable Long id, @RequestBody Asignatura datosNuevos) {
        return asignaturaRepo.findById(id).map(asignatura -> {
            asignatura.setNombre(datosNuevos.getNombre());
            return new ResponseEntity<>(asignaturaRepo.save(asignatura), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ELIMINAR ASIGNATURA
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAsignatura(@PathVariable Long id) {
        try {
            if (!asignaturaRepo.existsById(id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            asignaturaRepo.deleteById(id);
            return new ResponseEntity<>("Asignatura eliminada", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: No se puede borrar porque tiene temas o preguntas asociadas.", HttpStatus.CONFLICT);
        }
    }
}