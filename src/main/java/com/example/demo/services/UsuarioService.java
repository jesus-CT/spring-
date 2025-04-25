package com.example.demo.services;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.mapper.UsuarioMapper;
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
    private final UsuarioMapper mapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    public List<UsuarioDTO> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll(
                Sort.by(Sort.Direction.ASC, "apellidos")
                        .and(Sort.by("nombre"))
        );
        return mapper.toDtoList(usuarios);
    }

    public UsuarioDTO getUsuarioById(@NotNull Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con id " + id
                ));
        return mapper.toDto(usuario);
    }

//    public UsuarioDTO createUsuario(@NotNull @Valid UsuarioDTO dto) {
//        Usuario entidad = mapper.toEntity(dto);
//        entidad.setId(null); // asegurar que se genere un nuevo ID
//        Usuario guardado = usuarioRepository.save(entidad);
//        return mapper.toDto(guardado);
//    }

    public UsuarioDTO updateUsuario(@NotNull Long id, @NotNull @Valid UsuarioDTO dto) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con id " + id
                ));
        // actualizamos campos
        existente.setNombre(dto.getNombre());
        existente.setApellidos(dto.getApellidos());
        existente.setUsuario(dto.getUsuario());
        existente.setClave(dto.getClave());
        Usuario actualizado = usuarioRepository.save(existente);
        return mapper.toDto(actualizado);
    }

    public void deleteUsuario(@NotNull Long id) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con id " + id
                ));
        usuarioRepository.delete(existente);
    }
}
