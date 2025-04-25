package com.example.demo.dto;

import lombok.Data;

@Data
public class DiagnosticoDTO {
    private Long id;
    private String valoracionEspecialista;
    private String enfermedad;
    private Long citaId;
}