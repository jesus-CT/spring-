package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class MedicoDTO extends UsuarioDTO {
    @NotBlank(message = "El numColegiado es obligatorio")
    private String numColegiado;

    /**
     * IDs de pacientes asociados.
     */
    private List<Long> pacienteIds;
}