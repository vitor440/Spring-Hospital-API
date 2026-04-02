package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.model.Usuario;
import com.gerenciamento_hospitalar.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = repository.findByUsername(username);

        if(usuarioOpt.isPresent()) {
            return usuarioOpt.get();
        }
        else {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
    }
}
