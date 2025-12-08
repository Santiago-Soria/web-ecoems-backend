package escom.ipn.mx.ecoems.api.controller;

import escom.ipn.mx.ecoems.domain.entity.ExamenSimulacro;
import escom.ipn.mx.ecoems.domain.entity.Pregunta;
import escom.ipn.mx.ecoems.domain.repository.ExamenSimulacroRepository;
import escom.ipn.mx.ecoems.domain.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/examenes")
@CrossOrigin(origins = "*")
public class ExamenController {

    @Autowired
    private ExamenSimulacroRepository examenRepo;

    @Autowired
    private PreguntaRepository preguntaRepo;

    // 1. Listar todos los exámenes disponibles
    @GetMapping
    public List<ExamenSimulacro> listarExamenes() {
        return examenRepo.findAll();
    }

    // 2. Crear un nuevo examen
    @PostMapping
    public ResponseEntity<ExamenSimulacro> crearExamen(@RequestBody ExamenSimulacro examen) {
        return new ResponseEntity<>(examenRepo.save(examen), HttpStatus.CREATED);
    }

    // 3. Crear una pregunta para un examen (Simplificado)
    // Se espera que en el JSON venga el objeto "examen": {"idExamen": 1} y "asignatura": {"idAsignatura": 1}
    @PostMapping("/preguntas")
    public ResponseEntity<Pregunta> crearPregunta(@RequestBody Pregunta pregunta) {
        return new ResponseEntity<>(preguntaRepo.save(pregunta), HttpStatus.CREATED);
    }

    // 4. ¡IMPORTANTE! Obtener preguntas de un examen para resolverlo
    @GetMapping("/{idExamen}/preguntas")
    public List<Pregunta> obtenerPreguntasDeExamen(@PathVariable Long idExamen) {
        return preguntaRepo.findByExamen_IdExamen(idExamen);
    }

    // --- GESTIÓN DE EXÁMENES ---

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarExamen(@PathVariable Long id, @RequestBody ExamenSimulacro datos) {
        return examenRepo.findById(id).map(examen -> {
            examen.setNombre(datos.getNombre());
            return new ResponseEntity<>(examenRepo.save(examen), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarExamen(@PathVariable Long id) {
        try {
            if (!examenRepo.existsById(id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            examenRepo.deleteById(id);
            return new ResponseEntity<>("Examen eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("No se puede eliminar el examen porque ya tiene preguntas o resultados asociados.", HttpStatus.CONFLICT);
        }
    }

    // --- GESTIÓN DE PREGUNTAS (Corregir errores de dedo) ---

    @PutMapping("/preguntas/{idPregunta}")
    public ResponseEntity<?> actualizarPregunta(@PathVariable Long idPregunta, @RequestBody Pregunta datos) {
        return preguntaRepo.findById(idPregunta).map(pregunta -> {
            pregunta.setTexto(datos.getTexto());
            pregunta.setOpcionA(datos.getOpcionA());
            pregunta.setOpcionB(datos.getOpcionB());
            pregunta.setOpcionC(datos.getOpcionC());
            pregunta.setOpcionD(datos.getOpcionD());
            // Convertimos a minúscula por seguridad
            if(datos.getOpcionCorrecta() != null) {
                pregunta.setOpcionCorrecta(datos.getOpcionCorrecta().toLowerCase());
            }
            return new ResponseEntity<>(preguntaRepo.save(pregunta), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/preguntas/{idPregunta}")
    public ResponseEntity<?> eliminarPregunta(@PathVariable Long idPregunta) {
        if (!preguntaRepo.existsById(idPregunta)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        preguntaRepo.deleteById(idPregunta);
        return new ResponseEntity<>("Pregunta eliminada", HttpStatus.OK);
    }
}