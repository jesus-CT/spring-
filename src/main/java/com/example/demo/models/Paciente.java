package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@DiscriminatorValue("PACIENTE")
public class Paciente extends Usuario {

    @NotNull
    @Column(name = "nss", nullable = false, unique = true)
    private String NSS;

    @NotNull
    @Column(name = "num_tarjeta", nullable = false, unique = true)
    private String numTarjeta;

    @NotNull
    @Column(nullable = false)
    private String telefono;

    @NotNull
    @Column(nullable = false)
    private String direccion;

    @ManyToMany
    @JoinTable(
            name = "paciente_medico",
            joinColumns = @JoinColumn(name = "paciente_id"),
            inverseJoinColumns = @JoinColumn(name = "medico_id")
    )
    private Set<Medico> medicos = new HashSet<>();
}