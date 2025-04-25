package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String valoracionEspecialista;

    @NotNull
    @Column(nullable = false)
    private String enfermedad;

    @OneToOne
    @JoinColumn(name = "cita_id", nullable = false)
    @JsonBackReference
    private Cita cita;

}
