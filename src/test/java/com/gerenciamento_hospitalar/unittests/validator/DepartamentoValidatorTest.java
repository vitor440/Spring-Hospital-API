package com.gerenciamento_hospitalar.unittests.validator;

import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.validator.DepartamentoValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartamentoValidatorTest {

    @Mock
    DepartamentoRepository departamentoRepository;

    @Mock
    MedicoRepository medicoRepository;

    @InjectMocks
    DepartamentoValidator validator;

    Departamento departamento;

    Departamento departamentoSalvo;

    @BeforeEach
    void setUp() {
        departamento = mockDepartamento();
        departamentoSalvo = mockDepartamentoSalvo();
    }

    @Test
    void salvarDepartamentoSemNomeDuplicado() {
        // 1.cenário
        when(departamentoRepository.findByNome(departamento.getNome())).thenReturn(Optional.empty());

        // 2.execução e verificação
        assertDoesNotThrow(() -> validator.validar(departamento));

    }

    @Test
    void salvarDepartamentoComNomeDuplicado() {
        // 1.cenário
        when(departamentoRepository.findByNome(departamento.getNome())).thenReturn(Optional.of(departamentoSalvo));

        // 2.execução
        var resultado = catchThrowable(() -> validator.validar(departamento));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);
        assertThat(resultado).hasMessage("Não é permitido o cadastro de departamentos com o mesmo nome!");
    }

    @Test
    void AtualizarDepartamentoSemNomeDuplicado() {
        departamento.setId(2L);
        // 1.cenário
        when(departamentoRepository.findByNome(departamento.getNome())).thenReturn(Optional.empty());

        // 2.execução e verificação
        assertDoesNotThrow(() -> validator.validar(departamento));
    }

    @Test
    void AtualizarDepartamentoComNomeDuplicadoEIdIgual() {
        departamento.setId(1L);
        // 1.cenário
        when(departamentoRepository.findByNome(departamento.getNome())).thenReturn(Optional.of(departamentoSalvo));

        // 2.execução e verificação
        assertDoesNotThrow(() -> validator.validar(departamento));
    }

    @Test
    void AtualizarDepartamentoComNomeDuplicadoEIdDiferentes() {
        departamento.setId(2L);
        // 1.cenário
        when(departamentoRepository.findByNome(departamento.getNome())).thenReturn(Optional.of(departamentoSalvo));

        // 2.execução
        var resultado = catchThrowable(() -> validator.validar(departamento));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);
        assertThat(resultado).hasMessage("Não é permitido o cadastro de departamentos com o mesmo nome!");
    }



    @Test
    void deletarDepartamentoSemMedicosAssociados() {
        // 1.cenário
        when(medicoRepository.existsByDepartamento(departamentoSalvo)).thenReturn(false);

        // 2.execução e verificação
        assertDoesNotThrow(() -> validator.validarDelecao(departamentoSalvo));
    }

    @Test
    void deletarDepartamentoComMedicosAssociados() {
        // 1.cenário
        when(medicoRepository.existsByDepartamento(departamentoSalvo)).thenReturn(true);

        // 2.execução
        var resultado = catchThrowable(() -> validator.validarDelecao(departamentoSalvo));

        // 3.verificação
        assertThat(resultado).isInstanceOf(DelecaoNaoPermitidaException.class);
        assertThat(resultado).hasMessage("Não é permitido deletar departamentos com médicos associados!");
    }



    private Departamento mockDepartamento() {
        Departamento departamento1 = new Departamento();
        departamento1.setNome("Cardiologia");
        departamento1.setLocalizacao("quinto andar");

        return departamento1;
    }


    private Departamento mockDepartamentoSalvo() {
        Departamento departamento1 = mockDepartamento();
        departamento1.setId(1L);
        departamento1.setDataCriacao(LocalDateTime.of(2026, 4, 4, 14, 7, 30));

        return departamento1;
    }
}