package com.example.demo.repositories;

import com.example.demo.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsByNSS(String nss);
    boolean existsByNumTarjeta(String numTarjeta);
    boolean existsByUsuario(String usuario);

    boolean existsByNSSAndIdNot(String nss, Long id);
    boolean existsByNumTarjetaAndIdNot(String numTarjeta, Long id);
    boolean existsByUsuarioAndIdNot(String usuario, Long id);
}
