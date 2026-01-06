package escom.ipn.mx.ecoems.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subtema")
public class Subtema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subtema")
    private Long idSubtema;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "url_video")
    private String urlVideo;

    @Column(name = "orden")
    private Integer orden;

    @ManyToOne
    @JoinColumn(name = "id_tema", nullable = false)
    private Tema tema;
}