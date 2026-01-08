package escom.ipn.mx.ecoems.api.controller;

import escom.ipn.mx.ecoems.domain.entity.ExamenSimulacro;
import escom.ipn.mx.ecoems.domain.entity.ResultadoExamen;
import escom.ipn.mx.ecoems.domain.entity.Usuario;
import escom.ipn.mx.ecoems.domain.repository.ExamenSimulacroRepository;
import escom.ipn.mx.ecoems.domain.repository.ResultadoExamenRepository;
import escom.ipn.mx.ecoems.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import escom.ipn.mx.ecoems.domain.service.ReporteService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/resultados")
@CrossOrigin(origins = "*")
public class ResultadoController {

    @Autowired private ResultadoExamenRepository resultadoRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private ExamenSimulacroRepository examenRepo;
    @Autowired private ReporteService reporteService;

    // GUARDAR RESULTADO DE UN EXAMEN
    // JSON esperado: { "idUsuario": 1, "idExamen": 1, "scoreTotal": 8.5 }
    @PostMapping
    public ResponseEntity<?> guardarResultado(@RequestBody Map<String, Object> payload) {
        try {
            Long idUsuario = Long.valueOf(payload.get("idUsuario").toString());
            Long idExamen = Long.valueOf(payload.get("idExamen").toString());
            // Manejamos el score con cuidado por si viene como entero o decimal
            Double score = Double.valueOf(payload.get("scoreTotal").toString());

            Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
            ExamenSimulacro examen = examenRepo.findById(idExamen).orElse(null);

            if (usuario == null || examen == null) {
                return new ResponseEntity<>("Usuario o Examen no encontrado", HttpStatus.NOT_FOUND);
            }

            ResultadoExamen resultado = new ResultadoExamen();
            resultado.setUsuario(usuario);
            resultado.setExamen(examen);
            resultado.setScoreTotal(score);
            resultado.setFecha(new Date()); // Fecha actual automática

            return new ResponseEntity<>(resultadoRepo.save(resultado), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al guardar: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // VER HISTORIAL DE UN USUARIO
    @GetMapping("/usuario/{idUsuario}")
    public List<ResultadoExamen> verHistorial(@PathVariable Long idUsuario) {
        return resultadoRepo.findByUsuario_IdUsuario(idUsuario);
    }

    @GetMapping("/usuario/{idUsuario}/pdf")
    public ResponseEntity<?> descargarHistorialPdf(@PathVariable Long idUsuario) {
        // 1. Buscamos al usuario
        Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        // 2. Buscamos sus resultados
        List<ResultadoExamen> historial = resultadoRepo.findByUsuario_IdUsuario(idUsuario);

        // 3. Generamos el PDF
        ByteArrayInputStream bis = reporteService.generarHistorialPdf(usuario, historial);

        // 4. Preparamos la respuesta para descarga
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=historial_academico.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/usuario/{idUsuario}/examen/{idExamen}")
    public ResponseEntity<?> obtenerResultadoEspecifico(@PathVariable Long idUsuario, @PathVariable Long idExamen) {
        return resultadoRepo.findFirstByUsuario_IdUsuarioAndExamen_IdExamenOrderByFechaDesc(idUsuario, idExamen)
                .map(resultado -> new ResponseEntity<>(resultado, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); 
                // Si no hay resultado, el 404 ahora será "esperado" y no un error de ruta inexistente
    }
}