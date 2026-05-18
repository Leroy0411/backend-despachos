package com.citt.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "despachos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numeroDespacho;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String direccionDestino;

    @Column
    private String observaciones;

    @Column(nullable = false)
    private String fechaDespacho;

    @Column
    private String fechaCierre;
}
