package com.example.demo.dto;

import lombok.Data;
import java.util.Date;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
public class CitaDTO {
    private Long id;

    @NotNull(message = "La fecha y hora de la cita es obligatoria")
    private Date fechaHora;

    @NotNull(message = "El motivo de la cita es obligatorio")
    private String motivoCita;

    private int attribute11;

    @NotNull(message = "El id del paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El id del médico es obligatorio")
    private Long medicoId;

    @NotNull(message = "La cita debe incluir un diagnóstico")
    @Valid
    private DiagnosticoDTO diagnostico;
}
