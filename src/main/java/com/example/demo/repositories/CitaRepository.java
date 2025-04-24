package com.example.demo.repositories;

import com.example.demo.models.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    // Aquí puedes añadir consultas personalizadas si las necesitas, por ejemplo:
    // List<Cita> findByMotivoCitaContainingIgnoreCase(String motivo);
}
