package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MedicoDTO extends UsuarioDTO {
    private Long id;

    @NotBlank(message = "El numColegiado es obligatorio")
    private String numColegiado;

    private List<Long> pacienteIds;
}
