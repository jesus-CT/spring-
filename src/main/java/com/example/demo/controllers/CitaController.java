package com.example.demo.controllers;
import com.example.demo.models.Cita;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/cita")
public class CitaController {

    private List<Cita> citas = new ArrayList<>();

    @GetMapping
    public List<Cita> getAllCitas() {
        return citas;
    }

    @PostMapping
    public Cita createCita(@RequestBody Cita cita) {
        cita.setId((long) (citas.size() + 1));
        cita.setFechaHora(new Date());
        citas.add(cita);
        return cita;
    }
}
