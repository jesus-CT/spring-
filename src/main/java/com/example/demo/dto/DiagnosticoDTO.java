package com.example.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class DiagnosticoDTO {
    private Long id;

    @NotNull(message = "La valoración del especialista es obligatoria")
    private String valoracionEspecialista;

    @NotNull(message = "La enfermedad es obligatoria")
    private String enfermedad;

    private Long citaId;
}