package escom.ipn.mx.ecoems.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario") // Debe coincidir con el nombre en DBeaver
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "ap_paterno", nullable = false)
    private String apPaterno;

    @Column(name = "ap_materno", nullable = false)
    private String apMaterno;

    // OJO: En tu BD se llama 'correo', no 'email'
    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    // OJO: En tu BD se llama 'contrasena', no 'password'
    @Column(name = "contrasena", nullable = false)
    private String contrasena;
}