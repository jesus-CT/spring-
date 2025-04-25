package com.example.demo.dto;

import lombok.Data;
import java.util.Date;

@Data
public class CitaDTO {
    private Long id;
    private Date fechaHora;
    private String motivoCita;
    private int attribute11;
    private Long pacienteId;
    private Long medicoId;
}
