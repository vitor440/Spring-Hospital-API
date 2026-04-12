package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.model.Role;
import com.gerenciamento_hospitalar.repository.RoleRepository;
import com.gerenciamento_hospitalar.service.RolesService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesServiceTest {

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private RolesService service;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRole("admin");
    }

    @Test
    void createRole() {
        // 1.cenário
        when(repository.findByRole(role.getRole())).thenReturn(Optional.empty());
        when(repository.save(role)).thenReturn(role);

        // 2. execução
        var resultado = service.createRole(role);

        // 3. verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(role);
        verify(repository, times(1)).save(role);
    }

    @Test
    void createRoleJaExistente() {
        // 1.cenário
        when(repository.findByRole(role.getRole())).thenReturn(Optional.of(role));

        // 2. execução
        var resultado = catchThrowable(() -> service.createRole(role));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class).hasMessage("Já existe uma role com este nome!");
        verify(repository, times(0)).save(role);
    }


}