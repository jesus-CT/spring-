package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@DiscriminatorValue("MEDICO")
public class Medico extends Usuario {

    @NotNull
    @Column(name = "num_colegiado", nullable = false, unique = true)
    private String numColegiado;

    @ManyToMany(mappedBy = "medicos", fetch = FetchType.LAZY)
    private Set<Paciente> pacientes = new HashSet<>();

}
