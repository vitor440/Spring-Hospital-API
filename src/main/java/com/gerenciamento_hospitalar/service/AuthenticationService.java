package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.RoleDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.UsuarioMapper;
import com.gerenciamento_hospitalar.model.Role;
import com.gerenciamento_hospitalar.model.Usuario;
import com.gerenciamento_hospitalar.repository.UsuarioRepository;
import com.gerenciamento_hospitalar.repository.RoleRepository;
import com.gerenciamento_hospitalar.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final UsuarioMapper mapper;
    private final RoleRepository roleRepository;

    public TokenDTO authenticate(CadastroUsuarioDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.senha()
        ));

        var user = repository.findByUsername(request.username());

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Username not found!");
        }

        return jwtProvider.createAcessToken(request.username(), user.get().getRoles());
    }

    public TokenDTO refreshToken(String username, String refreshToken) {
        var user = repository.findByUsername(username);
        TokenDTO token;

        if(user.isPresent()) {
            token = jwtProvider.createRefreshToken(refreshToken);
        }
        else{
            throw new UsernameNotFoundException("Username not found!");
        }

        return token;
    }


    public CadastroUsuarioDTO cadastroUsuario(CadastroUsuarioDTO usuarioDTO) {

        String nomeCompleto = usuarioDTO.nomeCompleto();
        String username = usuarioDTO.username();
        String senha = encoder.encode(usuarioDTO.senha());

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setNomeCompleto(nomeCompleto);
        usuario.setSenha(senha);
        usuario.setAccountNonExpired(true);
        usuario.setAccountNonLocked(true);
        usuario.setCredentialsNonExpired(true);
        usuario.setEnabled(true);

        return mapper.toDTO(repository.save(usuario));
    }

    public void adicionaRole(Long userId, RoleDTO roleDTO) {
        Usuario usuario = repository.findById(userId)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Usuário não encontrado."));

        for (String role : roleDTO.roles()) {
            Optional<Role> roleOpt = roleRepository.findByRole(role);

            if(roleOpt.isPresent()) {
                usuario.getPermissions().add(roleOpt.get());
            }
            else {
                throw new RegistroNaoEncontradoException("Role " + role + " não encontrada.");
            }
        }

        repository.save(usuario);
    }
}
