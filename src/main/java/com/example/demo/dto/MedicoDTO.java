package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class MedicoDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String usuario;
    private String clave;
    private String numColegiado;

    private List<Long> pacienteIds;
}
