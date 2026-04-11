package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.ConsultaMapper;
import com.gerenciamento_hospitalar.mapper.MedicoMapper;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.model.Especialidade;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.repository.UsuarioRepository;
import com.gerenciamento_hospitalar.service.MedicoService;
import com.gerenciamento_hospitalar.service.SecurityService;
import com.gerenciamento_hospitalar.validator.MedicoValidator;
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
class MedicoServiceTest {

    @Mock
    MedicoRepository medicoRepository;

    @Mock
    MedicoValidator validator;

    @Mock
    MedicoMapper mapper;

    @Mock
    ConsultaMapper consultaMapper;

    @Mock
    DepartamentoRepository departamentoRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    MedicoService service;

    Medico medico;
    Medico medicoSalvo;
    MedicoRequest request;
    MedicoResponse response;

    @BeforeEach
    void setUp() {
        medico = mockMedico();
        medicoSalvo = mockMedicoSalvo();
        request = mockRequest();
        response = mockResponse();
    }

    @Test
    void addMedico() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(medico);
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(mockDepartamento()));
        doNothing().when(validator).validar(medico);
        when(medicoRepository.save(medico)).thenReturn(medicoSalvo);
        when(mapper.toDTO(medicoSalvo)).thenReturn(response);

        // 2. execução
        var resultado = service.addMedico(request);

        // 3.verificação
        assertEquals(resultado.id(), response.id());
        assertEquals(resultado.nome(), response.nome());
        assertEquals(resultado.crm(), response.crm());
        assertEquals(resultado.email(), response.email());
        assertEquals(resultado.telefone(), response.telefone());
        assertEquals(resultado.especialidade(), response.especialidade());
        assertEquals(resultado.departamentoId(), response.departamentoId());

        verify(medicoRepository, times(1)).save(medico);
    }

    @Test
    void addMedicoComDepartamentoInexistente() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(medico);
        doThrow(RegistroNaoEncontradoException.class).when(departamentoRepository).findById(1L);


        // 2. execução
        var resultado = catchThrowable(() -> service.addMedico(request));

        // 3.verificação

        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);
        verify(medicoRepository, times(0)).save(medico);
    }


    @Test
    void addMedicoComCrmDuplicado() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(medico);
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(mockDepartamento()));
        doThrow(RegistroDuplicadoException.class).when(validator).validar(medico);

        // 2. execução
        var resultado = catchThrowable(() -> service.addMedico(request));

        // 3.verificação

        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);
        verify(medicoRepository, times(0)).save(medico);
    }


    @Test
    void atualizarMedico() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoSalvo));
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(mockDepartamento()));
        doNothing().when(validator).validar(medicoSalvo);
        when(medicoRepository.save(medicoSalvo)).thenReturn(medicoSalvo);
        when(mapper.toDTO(medicoSalvo)).thenReturn(response);

        // 2. execução
        var resultado = service.atualizarMedico(1L, request);

        // 3.verificação
        assertEquals(resultado.id(), response.id());
        assertEquals(resultado.nome(), response.nome());
        assertEquals(resultado.crm(), response.crm());
        assertEquals(resultado.email(), response.email());
        assertEquals(resultado.telefone(), response.telefone());
        assertEquals(resultado.especialidade(), response.especialidade());
        assertEquals(resultado.departamentoId(), response.departamentoId());

        verify(medicoRepository, times(1)).save(medicoSalvo);
    }


    @Test
    void atualizarMedicoInexistente() {
        // 1.cenário
        doThrow(RegistroNaoEncontradoException.class).when(medicoRepository).findById(1L);

        // 2. execução
        var resultado = catchThrowable(() -> service.atualizarMedico(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);

        verify(medicoRepository, times(0)).save(medicoSalvo);
    }


    @Test
    void atualizarMedicoComDepartamentoInexistente() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoSalvo));
        doThrow(RegistroNaoEncontradoException.class).when(departamentoRepository).findById(1L);

        // 2. execução
        var resultado = catchThrowable(() -> service.atualizarMedico(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);

        verify(medicoRepository, times(0)).save(medicoSalvo);
    }


    @Test
    void atualizarMedicoComCrmDuplicado() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoSalvo));
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(mockDepartamento()));
        doThrow(RegistroDuplicadoException.class).when(validator).validar(medicoSalvo);

        // 2. execução
        var resultado = catchThrowable(() -> service.atualizarMedico(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);

        verify(medicoRepository, times(0)).save(medicoSalvo);
    }


    @Test
    void obterMedicoPeloId() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoSalvo));
        when(mapper.toDTO(medicoSalvo)).thenReturn(response);

        // 2. execução
        var resultado = service.obterMedicoPeloId(1L);

        // 3.verificação
        assertEquals(resultado.id(), response.id());
        assertEquals(resultado.nome(), response.nome());
        assertEquals(resultado.crm(), response.crm());
        assertEquals(resultado.email(), response.email());
        assertEquals(resultado.telefone(), response.telefone());
        assertEquals(resultado.especialidade(), response.especialidade());
        assertEquals(resultado.departamentoId(), response.departamentoId());

        verify(medicoRepository, times(1)).findById(1L);
        verify(mapper, times(1)).toDTO(medicoSalvo);
    }

    @Test
    void obterMedicoPeloIdInexistente() {
        // 1.cenário
        doThrow(RegistroNaoEncontradoException.class).when(medicoRepository).findById(1L);

        // 2. execução
        var resultado = catchThrowable(() -> service.obterMedicoPeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);

        verify(medicoRepository, times(1)).findById(1L);
        verify(mapper, times(0)).toDTO(medicoSalvo);
    }


    @Test
    void deletarMedicoPeloId() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoSalvo));
        doNothing().when(validator).validarDelecao(medicoSalvo);

        // 2. execução
        service.deletarMedicoPeloId(1L);

        // 3.verificação

        verify(medicoRepository, times(1)).findById(1L);
        verify(medicoRepository, times(1)).delete(medicoSalvo);
    }

    @Test
    void deletarMedicoPeloIdInexistente() {
        // 1.cenário
        doThrow(RegistroNaoEncontradoException.class).when(medicoRepository).findById(1L);

        // 2. execução
        var resultado = catchThrowable(() -> service.deletarMedicoPeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);
        verify(medicoRepository, times(1)).findById(1L);
        verify(medicoRepository, times(0)).delete(medicoSalvo);
    }


    @Test
    void deletarMedicoPeloIdComConsultasRegistradas() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoSalvo));
        doThrow(DelecaoNaoPermitidaException.class).when(validator).validarDelecao(medicoSalvo);

        // 2. execução
        var resultado = catchThrowable(() -> service.deletarMedicoPeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(DelecaoNaoPermitidaException.class);
        verify(medicoRepository, times(1)).findById(1L);
        verify(medicoRepository, times(0)).delete(medicoSalvo);
    }


    @Test
    void listarMedicos() {
    }


    private Medico mockMedico() {
        Medico medico1 = new Medico();
        medico1.setNome("Dr Robson Santos");
        medico1.setCrm("crm-23323");
        medico1.setEmail("robson@email.com");
        medico1.setTelefone("9999-9999");
        medico1.setEspecialidade(Especialidade.NEUROLOGISTA);
        medico1.setDepartamento(new Departamento(1L, "Neurologia", "sexto andar", LocalDateTime.of(2026, 4, 4, 14, 7, 30)));

        return medico1;
    }

    private Medico mockMedicoSalvo() {
        Medico medico1 = mockMedico();
        medico1.setId(1L);
        return medico1;
    }

    private MedicoRequest mockRequest() {
        Especialidade neurologista = Especialidade.NEUROLOGISTA;
        String crm = "crm-23323";
        String nome = "Dr Robson Santos";
        String email = "robson@email.com";
        String telefone = "9999-9999";

        MedicoRequest request = new MedicoRequest(crm, nome, email, telefone, neurologista, 1L, 1L);
        return request;
    }

    private MedicoResponse mockResponse() {
        Especialidade neurologista = Especialidade.NEUROLOGISTA;
        String crm = "crm-23323";
        String nome = "Dr Robson Santos";
        String email = "robson@email.com";
        String telefone = "9999-9999";

        MedicoResponse request = new MedicoResponse(1L, crm, nome, email, telefone, neurologista, 1L);
        return request;
    }

    private Departamento mockDepartamento() {
        Departamento departamento1 = new Departamento();
        departamento1.setId(1L);
        departamento1.setNome("Cardiologia");
        departamento1.setLocalizacao("quinto andar");

        return departamento1;
    }

}