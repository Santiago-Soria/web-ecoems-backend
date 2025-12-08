package escom.ipn.mx.ecoems.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pregunta")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Long idPregunta;

    @Column(name = "texto", nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Column(name = "opcion_a", nullable = false)
    private String opcionA;

    @Column(name = "opcion_b", nullable = false)
    private String opcionB;

    @Column(name = "opcion_c", nullable = false)
    private String opcionC;

    @Column(name = "opcion_d", nullable = false)
    private String opcionD;

    @Column(name = "opcion_correcta", nullable = false, length = 1)
    private String opcionCorrecta; // Esperamos 'A', 'B', 'C' o 'D'

    @PrePersist
    public void prePersist() {
        if (this.opcionCorrecta != null) {
            this.opcionCorrecta = this.opcionCorrecta.toLowerCase();
        }
    }

    // Relación: Una pregunta pertenece a un Examen
    @ManyToOne
    @JoinColumn(name = "id_examen", nullable = false)
    private ExamenSimulacro examen;

    // Relación: Una pregunta pertenece a una Asignatura (Tema)
    @ManyToOne
    @JoinColumn(name = "id_asignatura", nullable = false)
    private Asignatura asignatura;
}