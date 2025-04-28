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
    @ManyToMany(mappedBy = "medicos", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Paciente> pacientes = new HashSet<>();

    public void addPaciente(Paciente p) {
        pacientes.add(p);
        p.getMedicos().add(this);
    }

    public void removePaciente(Paciente p) {
        pacientes.remove(p);
        p.getMedicos().remove(this);
    }
}