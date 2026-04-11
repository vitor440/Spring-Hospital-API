package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.DepartamentoMapper;
import com.gerenciamento_hospitalar.mocks.DepartamentoMock;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import com.gerenciamento_hospitalar.service.DepartamentoService;
import com.gerenciamento_hospitalar.validator.DepartamentoValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartamentoServiceTest {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private DepartamentoValidator validator;

    @Mock
    private DepartamentoMapper mapper;

    @InjectMocks
    private DepartamentoService service;


    private DepartamentoRequest request;

    private Departamento departamento;

    private DepartamentoResponse response;

    @BeforeEach
    void setUp() {
        request = DepartamentoMock.mockRequest(1);
        departamento = DepartamentoMock.mockDepartamento(1);
        response = DepartamentoMock.mockResponse(1);
    }

    @Test
    void addDepartamento() {

        // 1.cenário
        Departamento departamentoSalvo = departamento;
        departamentoSalvo.setId(1L);

        when(mapper.toEntity(request)).thenReturn(departamento);
        when(departamentoRepository.save(departamento)).thenReturn(departamentoSalvo);
        when(mapper.toDTO(departamentoSalvo)).thenReturn(response);

        // 2.execução
        var resultado = service.addDepartamento(request);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);

        verify(validator, times(1)).validar(departamento);
        verify(departamentoRepository, times(1)).save(departamento);
    }

    @Test
    void addDepartamentoComNomeDuplicado() {

        // 1.cenário
        Departamento departamentoSalvo = departamento;
        departamentoSalvo.setId(1L);

        when(mapper.toEntity(request)).thenReturn(departamento);
        doThrow(RegistroDuplicadoException.class).when(validator).validar(departamento);

        // 2.execução
        var resultado = catchThrowable(() -> service.addDepartamento(request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);

        verify(validator, times(1)).validar(departamento);
        verify(departamentoRepository, times(0)).save(departamento);
    }



    @Test
    void atualizarDepartamento() {

        // 1.cenário
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(departamentoRepository.save(departamento)).thenReturn(departamento);
        when(mapper.toDTO(departamento)).thenReturn(response);

        // 2.execução
        var resultado = service.atualizarDepartamento(1L, request);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);

        verify(validator, times(1)).validar(departamento);
        verify(departamentoRepository, times(1)).save(departamento);

    }

    @Test
    void atualizarDepartamentoInexistente() {

        // 1.cenário
        when(departamentoRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.atualizarDepartamento(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe departamento com esse ID!");

        verify(validator, times(0)).validar(departamento);
        verify(departamentoRepository, times(0)).save(departamento);

    }

    @Test
    void atualizarDepartamentoComNomeDuplicado() {

        // 1.cenário
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        doThrow(RegistroDuplicadoException.class).when(validator).validar(departamento);

        // 2.execução
        var resultado = catchThrowable(() -> service.atualizarDepartamento(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);

        verify(validator, times(1)).validar(departamento);
        verify(departamentoRepository, times(0)).save(departamento);

    }



    @Test
    void obterDepartamentoPeloId() {

        // 1.cenário
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(mapper.toDTO(departamento)).thenReturn(response);

        // 2.execução
        var resultado = service.obterDepartamentoPeloId(1L);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);

        verify(mapper, times(1)).toDTO(departamento);

    }

    @Test
    void obterDepartamentoInexistente() {

        // 1.cenário
        when(departamentoRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.obterDepartamentoPeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe departamento com esse ID!");

        verify(mapper, times(0)).toDTO(departamento);

    }



    @Test
    void deletarDepartamentoPeloId() {

        // 1.cenário
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));

        // 2.execução
        service.deletarDepartamentoPeloId(1L);

        // 3.verificação
        verify(validator, times(1)).validarDelecao(departamento);
        verify(departamentoRepository, times(1)).delete(departamento);
    }


    @Test
    void deletarDepartamentoInexistente() {

        // 1.cenário
        when(departamentoRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.deletarDepartamentoPeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe departamento com esse ID!");

        verify(validator, times(0)).validarDelecao(departamento);
        verify(departamentoRepository, times(0)).delete(departamento);
    }

    @Test
    void deletarDepartamentoComMedicosAssociados() {

        // 1.cenário
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        doThrow(DelecaoNaoPermitidaException.class).when(validator).validarDelecao(departamento);

        // 2.execução
        var resultado = catchThrowable(() -> service.deletarDepartamentoPeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(DelecaoNaoPermitidaException.class);

        verify(validator, times(1)).validarDelecao(departamento);
        verify(departamentoRepository, times(0)).delete(departamento);
    }



}