package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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

    // Relación hacia Paciente (muchas citas → un paciente)
    @NotNull(message = "La cita debe tener asignado un paciente")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    @JsonBackReference("paciente-citas")
    private Paciente paciente;

    // Relación hacia Médico (muchas citas → un médico)
    @NotNull(message = "La cita debe tener asignado un médico")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    @JsonBackReference("medico-citas")
    private Medico medico;

    @NotNull(message = "La cita debe tener un diagnóstico")
    @Valid
    @OneToOne(
            mappedBy = "cita",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            optional = false
    )
    @JsonManagedReference("cita-diagnostico")
    private Diagnostico diagnostico;
}


