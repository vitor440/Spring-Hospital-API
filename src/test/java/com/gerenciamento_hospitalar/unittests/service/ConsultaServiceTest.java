package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.request.ConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.mapper.ConsultaMapper;
import com.gerenciamento_hospitalar.mocks.ConsultaMock;
import com.gerenciamento_hospitalar.mocks.MedicoMock;
import com.gerenciamento_hospitalar.mocks.PacienteMock;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.repository.PacienteRepository;
import com.gerenciamento_hospitalar.service.ConsultaService;
import com.gerenciamento_hospitalar.validator.ConsultaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock
    ConsultaRepository consultaRepository;

    @Mock
    MedicoRepository medicoRepository;

    @Mock
    PacienteRepository pacienteRepository;

    @Mock
    ConsultaValidator validator;

    @Mock
    ConsultaMapper mapper;

    @InjectMocks
    ConsultaService service;

    private Consulta consulta;
    private Consulta consultaSalva;
    private ConsultaRequest request;
    private ConsultaResponse response;

    @BeforeEach
    void setUp() {
        consulta = ConsultaMock.mockConsulta(1);
        consultaSalva = ConsultaMock.mockConsultaSalva(1);
        request = ConsultaMock.mockConsultaRequest(1);
        response = ConsultaMock.mockConsultaResponse(1);
    }

    @Test
    void agendarConsulta() {
        // 1.cénario
        when(mapper.toEntity(request)).thenReturn(consulta);
        when(medicoRepository.findById(request.medicoId())).thenReturn(Optional.of(MedicoMock.mockMedicoSalvo(1)));
        when(pacienteRepository.findById(request.pacienteId())).thenReturn(Optional.of(PacienteMock.mockPacienteSalvo(1)));
        doNothing().when(validator).validar(consulta);
        when(consultaRepository.save(consulta)).thenReturn(consultaSalva);
        when(mapper.toDTO(consultaSalva)).thenReturn(response);

        // 2.execução
        var resultado = service.agendarConsulta(request);

        // 3. verificação
        assertNotNull(resultado.id());

        assertEquals(resultado.diaSemana(), response.diaSemana());
        assertEquals(resultado.data(), response.data());
        assertEquals(resultado.hora(), response.hora());
        assertEquals(resultado.status(), response.status());
        assertEquals(resultado.proposito(), response.proposito());
        assertEquals(resultado.medicoId(), response.medicoId());
        assertEquals(resultado.pacienteId(), response.pacienteId());
    }

    @Test
    void atualizarConsulta() {
        // 1.cénario
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consultaSalva));
        when(medicoRepository.findById(request.medicoId())).thenReturn(Optional.of(MedicoMock.mockMedicoSalvo(1)));
        when(pacienteRepository.findById(request.pacienteId())).thenReturn(Optional.of(PacienteMock.mockPacienteSalvo(1)));
        doNothing().when(validator).validar(consultaSalva);
        when(consultaRepository.save(consultaSalva)).thenReturn(consultaSalva);
        when(mapper.toDTO(consultaSalva)).thenReturn(response);

        // 2.execução
        var resultado = service.atualizarConsulta(1L, request);

        // 3. verificação
        assertNotNull(resultado.id());

        assertEquals(resultado.diaSemana(), response.diaSemana());
        assertEquals(resultado.data(), response.data());
        assertEquals(resultado.hora(), response.hora());
        assertEquals(resultado.status(), response.status());
        assertEquals(resultado.proposito(), response.proposito());
        assertEquals(resultado.medicoId(), response.medicoId());
        assertEquals(resultado.pacienteId(), response.pacienteId());

    }

    @Test
    void listarConsultas() {
    }

    @Test
    void obterConsultaPeloId() {
    }

    @Test
    void modificaStatusConsulta() {
    }

    @Test
    void deletarConsultaPeloId() {
    }
}