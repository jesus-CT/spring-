package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UsuarioDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    private String apellidos;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El usuario debe tener entre 4 y 20 caracteres")
    private String usuario;

    @NotBlank(message = "La clave es obligatoria")
    private String clave;

    private String tipoUsuario;
}
