package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

}
