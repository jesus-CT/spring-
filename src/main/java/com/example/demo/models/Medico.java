package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@DiscriminatorValue("MEDICO")
public class Medico extends Usuario {

    @NotNull
    @Column(name = "num_colegiado", nullable = false, unique = true)
    private String numColegiado;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "medicos")
    private Set<Paciente> pacientes = new HashSet<>();
}