package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class PacienteDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String usuario;
    private String clave;
    private String NSS;
    private String numTarjeta;
    private String telefono;
    private String direccion;

    private List<Long> medicoIds;
}