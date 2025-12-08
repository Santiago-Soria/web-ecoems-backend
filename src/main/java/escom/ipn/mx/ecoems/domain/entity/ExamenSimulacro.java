package escom.ipn.mx.ecoems.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "examen_simulacro")
public class ExamenSimulacro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_examen")
    private Long idExamen;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}