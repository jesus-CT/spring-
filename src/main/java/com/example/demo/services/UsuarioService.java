package com.example.demo.services;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UsuarioService
        extends AbstractService<UsuarioDTO, Usuario, Long> {

    public UsuarioService(UsuarioRepository repo,
                          UsuarioMapper mapper) {
        // ojo: pasamos también la clase para mensajes
        super(repo, mapper, Usuario.class);
    }
}
