package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.MedicamentoMapper;
import com.gerenciamento_hospitalar.mapper.ResultadoConsultaMapper;
import com.gerenciamento_hospitalar.mocks.ConsultaMock;
import com.gerenciamento_hospitalar.mocks.ResultadoConsultaMock;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.ResultadoConsulta;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.ResultadoConsultaRepository;
import com.gerenciamento_hospitalar.service.ResultadoConsultaService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultadoConsultaServiceTest {

    @Mock
    private ResultadoConsultaRepository resultadoConsultaRepository;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private ResultadoConsultaMapper mapper;

    @Mock
    private MedicamentoMapper medicamentoMapper;

    @InjectMocks
    private ResultadoConsultaService service;

    private ResultadoConsulta resultadoConsulta;
    private ResultadoConsultaRequest request;
    private ResultadoConsultaResponse response;
    private Consulta consulta;

    @BeforeEach
    void setUp() {
        resultadoConsulta = ResultadoConsultaMock.mockResultadoConsulta(1);
        request = ResultadoConsultaMock.mockRequest(1);
        response = ResultadoConsultaMock.mockResponse(1);
        consulta = ConsultaMock.mockConsulta(1);

    }

    @Test
    void gerarResultadoDaConsulta() {
        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(mapper.toEntity(request)).thenReturn(resultadoConsulta);
        when(resultadoConsultaRepository.save(resultadoConsulta)).thenReturn(resultadoConsulta);
        when(mapper.toDTO(resultadoConsulta)).thenReturn(response);

        // 2.execução
        var resultado = service.gerarResultadoDaConsulta(request, 1L);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(resultadoConsultaRepository, times(1)).save(resultadoConsulta);
        verify(mapper, times(1)).toDTO(resultadoConsulta);
    }

    @Test
    void gerarResultadoDaConsultaInexistente() {
        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.gerarResultadoDaConsulta(request, 1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe consulta com esse ID!");
        verify(resultadoConsultaRepository, times(0)).save(resultadoConsulta);
        verify(mapper, times(0)).toDTO(resultadoConsulta);
    }


    @Test
    void gerarResultadoDaConsultaQueJaPossuiResultado() {
        // 1.cenário
        consulta.setResultadoConsulta(resultadoConsulta);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));


        // 2.execução
        var resultado = catchThrowable(() -> service.gerarResultadoDaConsulta(request, 1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class).hasMessage("Esta consulta já possui resultado!");
        verify(resultadoConsultaRepository, times(0)).save(resultadoConsulta);
        verify(mapper, times(0)).toDTO(resultadoConsulta);
    }


    @Test
    void atualizarResultadoDaConsulta() {

        // 1.cenário
        when(resultadoConsultaRepository.findById(1L)).thenReturn(Optional.of(resultadoConsulta));
        when(resultadoConsultaRepository.save(resultadoConsulta)).thenReturn(resultadoConsulta);
        when(mapper.toDTO(resultadoConsulta)).thenReturn(response);

        // 2.execução
        var resultado = service.atualizarResultadoDaConsulta(1L, request);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(resultadoConsultaRepository, times(1)).save(resultadoConsulta);
        verify(mapper, times(1)).toDTO(resultadoConsulta);
    }

    @Test
    void atualizarResultadoInexistenteDaConsulta() {

        // 1.cenário
        when(resultadoConsultaRepository.findById(1L)).thenReturn(Optional.empty());


        // 2.execução
        var resultado = catchThrowable(() -> service.atualizarResultadoDaConsulta(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe resultado de consulta com esse ID!");
        verify(resultadoConsultaRepository, times(0)).save(resultadoConsulta);
        verify(mapper, times(0)).toDTO(resultadoConsulta);
    }



    @Test
    void deletarResultadoDaConsulta() {

        // 1.cenário
        when(resultadoConsultaRepository.findById(1L)).thenReturn(Optional.of(resultadoConsulta));


        // 2.execução
        service.deletarResultadoDaConsulta(1L);

        // 3.verificação
        verify(resultadoConsultaRepository, times(1)).delete(resultadoConsulta);
    }

    @Test
    void deletarResultadoDaConsultaInexistente() {

        // 1.cenário
        when(resultadoConsultaRepository.findById(1L)).thenReturn(Optional.empty());


        // 2.execução
        var resultado = catchThrowable(() -> service.deletarResultadoDaConsulta(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe resultado de consulta com esse ID!");
        verify(resultadoConsultaRepository, times(0)).delete(resultadoConsulta);
    }



    @Test
    void obterResultadoPeloIdDaConsulta() {

        // 1.cenário
        consulta.setResultadoConsulta(resultadoConsulta);
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(mapper.toDTO(resultadoConsulta)).thenReturn(response);

        // 2.execução
        var resultado = service.obterResultadoPeloIdDaConsulta(1L);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(mapper, times(1)).toDTO(resultadoConsulta);
    }

    @Test
    void obterResultadoDeConsultaSemResultado() {

        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        // 2.execução
        var resultado = catchThrowable(() -> service.obterResultadoPeloIdDaConsulta(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe resultado para essa consulta!");
        verify(mapper, times(0)).toDTO(resultadoConsulta);
    }



    @Test
    void obterResultadoPeloIdDaConsultaInexistente() {

        // 1.cenário
        consulta.setResultadoConsulta(resultadoConsulta);
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.obterResultadoPeloIdDaConsulta(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe consulta com esse ID!");
        verify(mapper, times(0)).toDTO(resultadoConsulta);

    }
    @Test
    void obterResultadoPeloId() {

        // 1.cenário
        when(resultadoConsultaRepository.findById(1L)).thenReturn(Optional.of(resultadoConsulta));
        when(mapper.toDTO(resultadoConsulta)).thenReturn(response);

        // 2.execução
        var resultado = service.obterResultadoPeloId(1L);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(mapper, times(1)).toDTO(resultadoConsulta);
    }


    @Test
    void obterResultadoInexistente() {

        // 1.cenário
        when(resultadoConsultaRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.obterResultadoPeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe resultado de consulta com esse ID!");
        verify(mapper, times(0)).toDTO(resultadoConsulta);
    }

}