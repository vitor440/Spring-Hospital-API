package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.request.ConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.exception.HorariosConflitantesException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.ConsultaMapper;
import com.gerenciamento_hospitalar.mocks.ConsultaMock;
import com.gerenciamento_hospitalar.mocks.MedicoMock;
import com.gerenciamento_hospitalar.mocks.PacienteMock;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.model.StatusConsulta;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.repository.PacienteRepository;
import com.gerenciamento_hospitalar.service.ConsultaService;
import com.gerenciamento_hospitalar.service.SecurityService;
import com.gerenciamento_hospitalar.validator.ConsultaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ConsultaValidator validator;

    @Mock
    private ConsultaMapper mapper;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private ConsultaService service;

    private Consulta consulta;
    private ConsultaRequest request;
    private ConsultaResponse response;
    private Paciente paciente;
    private Medico medico;

    @BeforeEach
    void setUp() {

        consulta = ConsultaMock.mockConsulta(1);
        request = ConsultaMock.mockConsultaRequest(1);
        response = ConsultaMock.mockConsultaResponse(1);
        paciente = PacienteMock.mockPaciente(1);
        medico = MedicoMock.mockMedico(1);
    }

    @Test
    void agendarConsulta() {

        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(consulta);
        when(medicoRepository.findById(request.medicoId())).thenReturn(Optional.of(medico));
        when(pacienteRepository.findById(request.pacienteId())).thenReturn(Optional.of(paciente));
        when(consultaRepository.save(consulta)).thenReturn(consulta);
        when(mapper.toDTO(consulta)).thenReturn(response);

        // 2.execução
        var resultado = service.agendarConsulta(request);

        // 3. verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(validator, times(1)).validar(consulta);
        verify(consultaRepository, times(1)).save(consulta);
    }

    @Test
    void agendarConsultaComMedicoInexistente() {

        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(consulta);
        when(medicoRepository.findById(request.medicoId())).thenReturn(Optional.empty());


        // 2.execução
        var resultado =  catchThrowable(() -> service.agendarConsulta(request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe médico com esse ID!");
        verify(validator, times(0)).validar(consulta);
        verify(consultaRepository, times(0)).save(consulta);
    }


    @Test
    void agendarConsultaComPacienteInexistente() {

        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(consulta);
        when(medicoRepository.findById(request.medicoId())).thenReturn(Optional.of(medico));
        when(pacienteRepository.findById(request.pacienteId())).thenReturn(Optional.empty());

        // 2.execução
        var resultado =  catchThrowable(() -> service.agendarConsulta(request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe paciente com esse ID!");
        verify(validator, times(0)).validar(consulta);
        verify(consultaRepository, times(0)).save(consulta);
    }


    @Test
    void agendarConsultaComDatasConflitantes() {

        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(consulta);
        when(medicoRepository.findById(request.medicoId())).thenReturn(Optional.of(medico));
        when(pacienteRepository.findById(request.pacienteId())).thenReturn(Optional.of(paciente));
        doThrow(HorariosConflitantesException.class).when(validator).validar(consulta);

        // 2.execução
        var resultado =  catchThrowable(() -> service.agendarConsulta(request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(HorariosConflitantesException.class);
        verify(validator, times(1)).validar(consulta);
        verify(consultaRepository, times(0)).save(consulta);
    }




    @Test
    void atualizarConsulta() {

        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(medicoRepository.findById(request.medicoId())).thenReturn(Optional.of(medico));
        when(pacienteRepository.findById(request.pacienteId())).thenReturn(Optional.of(paciente));
        when(consultaRepository.save(consulta)).thenReturn(consulta);
        when(mapper.toDTO(consulta)).thenReturn(response);

        // 2.execução
        var resultado = service.atualizarConsulta(1L, request);

        // 3. verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(validator, times(1)).validar(consulta);
        verify(consultaRepository, times(1)).save(consulta);
    }


    @Test
    void atualizarConsultaInexistente() {

        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado =  catchThrowable(() -> service.atualizarConsulta(1L, request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe consulta com esse ID!");
        verify(validator, times(0)).validar(consulta);
        verify(consultaRepository, times(0)).save(consulta);
    }


    @Test
    void atualizarConsultaComMedicoInexistente() {

        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(medicoRepository.findById(request.medicoId())).thenReturn(Optional.empty());

        // 2.execução
        var resultado =  catchThrowable(() -> service.atualizarConsulta(1L, request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe médico com esse ID!");
        verify(validator, times(0)).validar(consulta);
        verify(consultaRepository, times(0)).save(consulta);
    }


    @Test
    void atualizarConsultaComPacienteInexistente() {

        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(medicoRepository.findById(request.medicoId())).thenReturn(Optional.of(medico));
        when(pacienteRepository.findById(request.pacienteId())).thenReturn(Optional.empty());

        // 2.execução
        var resultado =  catchThrowable(() -> service.atualizarConsulta(1L, request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe paciente com esse ID!");
        verify(validator, times(0)).validar(consulta);
        verify(consultaRepository, times(0)).save(consulta);
    }


    @Test
    void atualizarConsultaComDatasConflitantes() {

        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(medicoRepository.findById(request.medicoId())).thenReturn(Optional.of(medico));
        when(pacienteRepository.findById(request.pacienteId())).thenReturn(Optional.of(paciente));
        doThrow(HorariosConflitantesException.class).when(validator).validar(consulta);

        // 2.execução
        var resultado =  catchThrowable(() -> service.atualizarConsulta(1L, request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(HorariosConflitantesException.class);
        verify(validator, times(1)).validar(consulta);
        verify(consultaRepository, times(0)).save(consulta);
    }





    @Test
    void obterConsultaPeloId() {
        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(mapper.toDTO(consulta)).thenReturn(response);

        // 2.execução
        var resultado = service.obterConsultaPeloId(1L);

        // 3. verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(consultaRepository, times(1)).findById(1L);
        verify(mapper, times(1)).toDTO(consulta);

    }

    @Test
    void obterConsultaInexistente() {
        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado =  catchThrowable(() -> service.obterConsultaPeloId(1L));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe consulta com esse ID!");
        verify(consultaRepository, times(1)).findById(1L);
        verify(mapper, times(0)).toDTO(consulta);
    }



    @Test
    void modificaStatusConsulta() {
        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        // 2.execução
        service.modificaStatusConsulta(1L, StatusConsulta.REALIZADA);

        // 3. verificação
        verify(consultaRepository, times(1)).modificaStatusConsulta(1L, StatusConsulta.REALIZADA);
    }

    @Test
    void modificaStatusConsultaInexistente() {
        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado =  catchThrowable(() -> service.modificaStatusConsulta(1L, StatusConsulta.AGENDADA));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe consulta com esse ID!");
        verify(consultaRepository, times(0)).modificaStatusConsulta(1L, StatusConsulta.REALIZADA);
    }



    @Test
    void deletarConsultaPeloId() {
        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        // 2.execução
        service.deletarConsultaPeloId(1L);

        // 3. verificação
        verify(consultaRepository, times(1)).delete(consulta);
    }

    @Test
    void deletarConsultaInexistente() {
        // 1.cenário
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado =  catchThrowable(() -> service.deletarConsultaPeloId(1L));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe consulta com esse ID!");
        verify(consultaRepository, times(0)).delete(consulta);
    }


}