package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class PacienteDTO extends UsuarioDTO {
    private Long id;

    @NotBlank(message = "El NSS es obligatorio")
    private String NSS;

    @NotBlank(message = "El número de tarjeta es obligatorio")
    private String numTarjeta;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private List<@NotNull(message = "El id de médico no puede ser nulo") Long> medicoIds;
}
