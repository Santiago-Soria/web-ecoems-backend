package escom.ipn.mx.ecoems.api.controller;

import escom.ipn.mx.ecoems.domain.entity.Subtema;
import escom.ipn.mx.ecoems.domain.repository.SubtemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subtemas")
@CrossOrigin(origins = "*")
public class SubtemaController {

    @Autowired
    private SubtemaRepository subtemaRepo;

    // Obtener subtemas de un tema específico
    @GetMapping("/tema/{idTema}")
    public List<Subtema> listarPorTema(@PathVariable Long idTema) {
        return subtemaRepo.findByTema_IdTemaOrderByOrdenAsc(idTema);
    }

    // Obtener un subtema por ID (para la vista de detalle)
    @GetMapping("/{id}")
    public ResponseEntity<Subtema> obtenerSubtema(@PathVariable Long id) {
        return subtemaRepo.findById(id)
                .map(subtema -> new ResponseEntity<>(subtema, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Crear subtema (Util para poblar la BD)
    @PostMapping
    public ResponseEntity<Subtema> crearSubtema(@RequestBody Subtema subtema) {
        return new ResponseEntity<>(subtemaRepo.save(subtema), HttpStatus.CREATED);
    }
}