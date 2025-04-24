package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("MEDICO")
public class Medico extends Usuario {

    @NotNull
    @Column(name = "num_colegiado", nullable = false, unique = true)
    private String numColegiado;

}
