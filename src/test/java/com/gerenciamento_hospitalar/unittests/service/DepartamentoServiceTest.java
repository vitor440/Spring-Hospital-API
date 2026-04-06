package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.DepartamentoMapper;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartamentoServiceTest {

    @Mock
    DepartamentoRepository repository;

    @Mock
    DepartamentoValidator validator;

    @Mock
    DepartamentoMapper mapper;

    @InjectMocks
    DepartamentoService service;

    Departamento departamento;
    Departamento departamentoSalvo;
    DepartamentoRequest request;
    DepartamentoResponse response;

    @BeforeEach
    void setUp() {
        departamento = mockDepartamento();
        departamentoSalvo = mockDepartamentoSalvo();
        request = mockRequest();
        response = mockResponse();
    }


    @Test
    void addDepartamento() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(departamento);
        doNothing().when(validator).validar(departamento);
        when(repository.save(departamento)).thenReturn(departamentoSalvo);
        when(mapper.toDTO(departamentoSalvo)).thenReturn(response);

        // 2. execução
        var resultado = service.addDepartamento(request);

        // 3. verificação
        assertNotNull(resultado);
        assertEquals(resultado.id(), response.id());
        assertEquals(resultado.nome(), response.nome());
        assertEquals(resultado.localizacao(), response.localizacao());
        assertEquals(resultado.dataCriacao(), response.dataCriacao());

        Mockito.verify(repository, times(1)).save(departamento);

    }

    @Test
    void addDepartamentoComNomeDuplicado() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(departamento);
        doThrow(RegistroDuplicadoException.class).when(validator).validar(departamento);

        // 2. execução
        var resultado = catchThrowable(() -> service.addDepartamento(request)) ;

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);
        Mockito.verify(repository, times(0)).save(departamento);

    }



    @Test
    void atualizarDepartamento() {

        // 1.cenário
        when(repository.findById(1L)).thenReturn(Optional.of(departamentoSalvo));
        doNothing().when(validator).validar(departamentoSalvo);
        when(repository.save(departamentoSalvo)).thenReturn(departamentoSalvo);
        when(mapper.toDTO(departamentoSalvo)).thenReturn(response);

        // 2. execução
        var resultado = service.atualizarDepartamento(1L, request);

        // 3. verificação
        assertNotNull(resultado);
        assertEquals(resultado.id(), response.id());
        assertEquals(resultado.nome(), response.nome());
        assertEquals(resultado.localizacao(), response.localizacao());
        assertEquals(resultado.dataCriacao(), response.dataCriacao());

        Mockito.verify(repository, times(1)).save(departamentoSalvo);
    }

    @Test
    void atualizarDepartamentoComNomeDuplicado() {

        // 1.cenário
        when(repository.findById(1L)).thenReturn(Optional.of(departamentoSalvo));
        doThrow(RegistroDuplicadoException.class).when(validator).validar(any());

        // 2. execução
        var resultado = catchThrowable(() -> service.atualizarDepartamento(1L, request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);

        Mockito.verify(repository, times(0)).save(departamentoSalvo);
    }

    @Test
    void atualizarDepartamentoInexistente() {

        // 1.cenário
        doThrow(RegistroNaoEncontradoException.class).when(repository).findById(1L);

        // 2. execução
        var resultado = catchThrowable(() -> service.atualizarDepartamento(1L, request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);

        Mockito.verify(repository, times(0)).save(departamentoSalvo);
    }



    @Test
    void obterDepartamentoPeloId() {
        // 1.cenário
        when(repository.findById(1L)).thenReturn(Optional.of(departamentoSalvo));
        when(mapper.toDTO(departamentoSalvo)).thenReturn(response);

        // 2. execução
        var resultado = service.obterDepartamentoPeloId(1L);

        // 3. verificação
        assertNotNull(resultado);
        assertEquals(resultado.id(), response.id());
        assertEquals(resultado.nome(), response.nome());
        assertEquals(resultado.localizacao(), response.localizacao());
        assertEquals(resultado.dataCriacao(), response.dataCriacao());

        Mockito.verify(repository, times(1)).findById(1L);
    }

    @Test
    void obterDepartamentoPeloIdInexistente() {
        // 1.cenário
        doThrow(RegistroNaoEncontradoException.class).when(repository).findById(1L);

        // 2. execução
        var resultado = catchThrowable(() -> service.obterDepartamentoPeloId(1L));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);

        Mockito.verify(mapper, times(0)).toDTO(any());
    }



    @Test
    void deletarDepartamentoPeloId() {
        // 1.cenário
        when(repository.findById(1L)).thenReturn(Optional.of(departamentoSalvo));
        doNothing().when(validator).validarDelecao(departamentoSalvo);

        // 2. execução
        service.deletarDepartamentoPeloId(1L);

        // 3. verificação

        Mockito.verify(repository, times(1)).delete(departamentoSalvo);
    }

    @Test
    void deletarDepartamentoPeloIdComMedicosAssociados() {
        // 1.cenário
        when(repository.findById(1L)).thenReturn(Optional.of(departamentoSalvo));
        doThrow(DelecaoNaoPermitidaException.class).when(validator).validarDelecao(departamentoSalvo);

        // 2. execução
        var resultado = catchThrowable(() -> service.deletarDepartamentoPeloId(1L));

        // 3. verificação

        assertThat(resultado).isInstanceOf(DelecaoNaoPermitidaException.class);
        Mockito.verify(repository, times(0)).delete(departamentoSalvo);
    }

    @Test
    void deletarDepartamentoPeloIdInexistente() {
        // 1.cenário
        doThrow(RegistroNaoEncontradoException.class).when(repository).findById(1L);

        // 2. execução
        var resultado = catchThrowable(() -> service.deletarDepartamentoPeloId(1L));

        // 3. verificação

        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);
        Mockito.verify(repository, times(0)).delete(departamentoSalvo);
    }



    @Test
    void listarDepartamentos() {
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




    private DepartamentoRequest mockRequest() {
        DepartamentoRequest request1 = new DepartamentoRequest("Cardiologia", "quinto andar");
        return request1;
    }

    private DepartamentoResponse mockResponse() {
        DepartamentoResponse response1 = new DepartamentoResponse(1L, "Cardiologia", "quinto andar",
                LocalDateTime.of(2026, 4, 4, 14, 7, 30));

        return response1;
    }
}