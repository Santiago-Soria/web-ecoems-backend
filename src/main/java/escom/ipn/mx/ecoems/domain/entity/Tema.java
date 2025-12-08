package escom.ipn.mx.ecoems.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tema")
public class Tema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tema")
    private Long idTema;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "id_asignatura", nullable = false)
    private Asignatura asignatura;
}