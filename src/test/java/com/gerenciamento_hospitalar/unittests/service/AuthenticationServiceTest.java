package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.mapper.UsuarioMapper;
import com.gerenciamento_hospitalar.mocks.UsuarioMock;
import com.gerenciamento_hospitalar.model.Role;
import com.gerenciamento_hospitalar.model.Usuario;
import com.gerenciamento_hospitalar.repository.RoleRepository;
import com.gerenciamento_hospitalar.repository.UsuarioRepository;
import com.gerenciamento_hospitalar.security.jwt.JwtProvider;
import com.gerenciamento_hospitalar.service.AuthenticationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthenticationService service;

    private Usuario usuario;
    private CadastroUsuarioDTO usuarioDTO;
    private TokenDTO tokenDTO;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRole("admin");

        usuario = UsuarioMock.mockUsuario(1);
        usuario.setPermissions(List.of(role));
        usuarioDTO = new CadastroUsuarioDTO(1L, "admin", "admin123", "admin");
        tokenDTO = new TokenDTO("admin", true, new Date(), new Date(), "23434324324", "4324324234234");
    }

    @Test
    void authenticate() {

        // 1.cenário
        when(repository.findByUsername(usuarioDTO.username())).thenReturn(Optional.of(usuario));
        when(jwtProvider.createAcessToken(usuarioDTO.username(), usuario.getRoles())).thenReturn(tokenDTO);

        // 2.execução
        var resultado = service.authenticate(usuarioDTO);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(tokenDTO);
    }

    @Test
    void authenticateComUsuarioNaoEncontrado() {

        // 1.cenário
        when(repository.findByUsername(usuarioDTO.username())).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.authenticate(usuarioDTO));

        // 3.verificação
        assertThat(resultado).isInstanceOf(UsernameNotFoundException.class).hasMessage("Username not found!");
    }



    @Test
    void refreshToken() {

        // 1.cenário
        when(repository.findByUsername(usuarioDTO.username())).thenReturn(Optional.of(usuario));
        when(jwtProvider.createRefreshToken("123")).thenReturn(tokenDTO);

        // 2.execução
        var resultado = service.refreshToken(usuarioDTO.username(), "123");

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(tokenDTO);
    }

    @Test
    void refreshTokenComUsuarioInexistente() {

        // 1.cenário
        when(repository.findByUsername(usuarioDTO.username())).thenReturn(Optional.empty());


        // 2.execução
        var resultado = catchThrowable(() -> service.authenticate(usuarioDTO));

        // 3.verificação
        assertThat(resultado).isInstanceOf(UsernameNotFoundException.class).hasMessage("Username not found!");
    }



    @Test
    void cadastroUsuario() {

        // 1.cenário
        when(repository.existsByUsername(usuarioDTO.username())).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        when(mapper.toDTO(usuario)).thenReturn(usuarioDTO);

        // 2.execução
        var resultado = service.cadastroUsuario(usuarioDTO);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(usuarioDTO);
    }

    @Test
    void cadastroUsuarioComUsernameExistente() {

        // 1.cenário
        when(repository.existsByUsername(usuarioDTO.username())).thenReturn(true);

        // 2.execução
        var resultado = catchThrowable(() -> service.cadastroUsuario(usuarioDTO));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class).hasMessage("Já existe um usuário com o username: admin");
    }

}