package escom.ipn.mx.ecoems.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "respuesta_usuario")
public class RespuestaUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    private Long idRespuesta;

    @Column(name = "respuesta_seleccionada", length = 1)
    private String respuestaSeleccionada; // 'A', 'B',C' o 'D'

    @ManyToOne
    @JoinColumn(name = "id_resultado_examen", nullable = false)
    private ResultadoExamen resultadoExamen;

    @ManyToOne
    @JoinColumn(name = "id_pregunta", nullable = false)
    private Pregunta pregunta;
}