package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
@Entity
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;

    private String motivoCita;

    private int attribute11;





}


