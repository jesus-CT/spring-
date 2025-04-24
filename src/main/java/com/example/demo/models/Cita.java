package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;


@Data
@Entity
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fechaHora;

    @NotNull
    @Column(nullable = false)
    private String motivoCita;

    @Column(nullable = true)
    private int attribute11;

    @NotNull(message = "El diagnóstico es obligatorio")
    @Valid
    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Diagnostico diagnostico;
}


