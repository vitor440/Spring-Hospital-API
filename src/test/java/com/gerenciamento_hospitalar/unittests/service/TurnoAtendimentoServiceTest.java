package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.request.TurnoAtendimentoRequest;
import com.gerenciamento_hospitalar.dto.response.TurnoAtendimentoResponse;
import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.HorariosConflitantesException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.TurnoAtendimentoMapper;
import com.gerenciamento_hospitalar.mocks.MedicoMock;
import com.gerenciamento_hospitalar.mocks.TurnoMock;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.model.TurnoAtendimento;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.repository.TurnoAtendimentoRepository;
import com.gerenciamento_hospitalar.service.SecurityService;
import com.gerenciamento_hospitalar.service.TurnoAtendimentoService;
import com.gerenciamento_hospitalar.validator.TurnoAtendimentoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurnoAtendimentoServiceTest {

    @Mock
    private TurnoAtendimentoRepository turnoAtendimentoRepository;

    @Mock
    private TurnoAtendimentoValidator validator;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private TurnoAtendimentoMapper mapper;

    @InjectMocks
    private TurnoAtendimentoService service;

    private TurnoAtendimento turno;
    private TurnoAtendimentoRequest request;
    private TurnoAtendimentoResponse response;
    private Medico medico;

    @BeforeEach
    void setUp() {
        turno = TurnoMock.mockTurno(1);
        request = TurnoMock.mockTurnoRequest(1);
        response = TurnoMock.mockTurnoResponse(1);
        medico = MedicoMock.mockMedico(1);
    }

    @Test
    void addDisponibilidadeMedico() {

        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(turno);
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(turnoAtendimentoRepository.save(turno)).thenReturn(turno);
        when(mapper.toDTO(turno)).thenReturn(response);

        // 2.execução
        var resultado = service.addDisponibilidadeMedico(1L, request);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(validator, times(1)).validar(turno);
        verify(turnoAtendimentoRepository, times(1)).save(turno);

    }

    @Test
    void addDisponibilidadeComMedicoInexistente() {

        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(turno);
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() ->service.addDisponibilidadeMedico(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe médico com esse ID!");
        verify(validator, times(0)).validar(turno);
        verify(turnoAtendimentoRepository, times(0)).save(turno);

    }


    @Test
    void addDisponibilidadeMedicoComSobreposicaoDeHorarios() {

        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(turno);
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        doThrow(HorariosConflitantesException.class).when(validator).validar(turno);

        // 2.execução
        var resultado = catchThrowable(() ->service.addDisponibilidadeMedico(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(HorariosConflitantesException.class);
        verify(validator, times(1)).validar(turno);
        verify(turnoAtendimentoRepository, times(0)).save(turno);

    }

    @Test
    void atualizarDisponibilidadeMedico() {

        // 1.cenário
        when(turnoAtendimentoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoAtendimentoRepository.save(turno)).thenReturn(turno);
        when(mapper.toDTO(turno)).thenReturn(response);

        // 2.execução
        var resultado = service.atualizarDisponibilidadeMedico(1L, request);

        // 3.verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(validator, times(1)).validar(turno);
        verify(turnoAtendimentoRepository, times(1)).save(turno);
    }

    @Test
    void atualizarDisponibilidadeInexistente() {

        // 1.cenário
        when(turnoAtendimentoRepository.findById(1L)).thenReturn(Optional.empty());


        // 2.execução
        var resultado = catchThrowable(() ->service.atualizarDisponibilidadeMedico(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe disponibilidade com esse ID!");
        verify(validator, times(0)).validar(turno);
        verify(turnoAtendimentoRepository, times(0)).save(turno);
    }

    @Test
    void atualizarDisponibilidadeComSobreposicaoDeHorarios() {

        // 1.cenário
        when(turnoAtendimentoRepository.findById(1L)).thenReturn(Optional.of(turno));
        doThrow(HorariosConflitantesException.class).when(validator).validar(turno);


        // 2.execução
        var resultado = catchThrowable(() ->service.atualizarDisponibilidadeMedico(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(HorariosConflitantesException.class);
        verify(validator, times(1)).validar(turno);
        verify(turnoAtendimentoRepository, times(0)).save(turno);
    }



    @Test
    void obterTurnosDeMedicoPeloId() {

        // 1.cenário
        medico.setDisponibilidades(List.of(TurnoMock.mockTurno(1)));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(mapper.toDTO(turno)).thenReturn(response);

        // 2.execução
        var resultado = service.obterTurnosDeMedicoPeloId(1L, 0, 5);

        // 3.verificação
        assertThat(resultado.get(0)).usingRecursiveComparison().isEqualTo(response);
        verify(mapper, times(1)).toDTO(turno);
    }

    @Test
    void obterTurnosDeMedicoInexistente() {

        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.obterTurnosDeMedicoPeloId(1L, 0, 5));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe médico com esse ID!");
        verify(mapper, times(0)).toDTO(turno);
    }



    @Test
    void deletarTurnoPeloId() {

        // 1.cenário
        when(turnoAtendimentoRepository.findById(1L)).thenReturn(Optional.of(turno));

        // 2.execução
        service.deletarTurnoPeloId(1L);

        // 3.verificação
        verify(validator, times(1)).validarDelecao(turno);
        verify(turnoAtendimentoRepository, times(1)).delete(turno);
    }

    @Test
    void deletarTurnoPeloIdInexistente() {

        // 1.cenário
        when(turnoAtendimentoRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.deletarTurnoPeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe disponibilidade com esse ID!");
        verify(validator, times(0)).validarDelecao(turno);
        verify(turnoAtendimentoRepository, times(0)).delete(turno);
    }

    @Test
    void deletarTurnoQueJaPossuiConsultaMarcada() {

        // 1.cenário
        when(turnoAtendimentoRepository.findById(1L)).thenReturn(Optional.of(turno));
        doThrow(DelecaoNaoPermitidaException.class).when(validator).validarDelecao(turno);

        // 2.execução
        var resultado = catchThrowable(() -> service.deletarTurnoPeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(DelecaoNaoPermitidaException.class);
        verify(validator, times(1)).validarDelecao(turno);
        verify(turnoAtendimentoRepository, times(0)).delete(turno);
    }




}