package com.example.demo.services;

import com.example.demo.models.Usuario;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Service
@Validated
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll(Sort.by(Sort.Direction.ASC, "apellidos").and(Sort.by("nombre")));
    }

    public Usuario getUsuarioById(@NotNull Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con id " + id));
    }

    public Usuario createUsuario(@NotNull @Valid Usuario usuario) {
        usuario.setId(null);
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(@NotNull Long id, @NotNull @Valid Usuario datosNuevos) {
        Usuario existente = getUsuarioById(id);
        existente.setNombre(datosNuevos.getNombre());
        existente.setApellidos(datosNuevos.getApellidos());
        existente.setUsuario(datosNuevos.getUsuario());
        existente.setClave(datosNuevos.getClave());
        return usuarioRepository.save(existente);
    }

    public void deleteUsuario(@NotNull Long id) {
        Usuario existente = getUsuarioById(id);
        usuarioRepository.delete(existente);
    }
}
