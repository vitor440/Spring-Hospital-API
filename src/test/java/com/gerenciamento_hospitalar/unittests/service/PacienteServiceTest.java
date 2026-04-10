package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.exception.AcessoNegadoException;
import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.file.imports.factory.PacienteImporterFactory;
import com.gerenciamento_hospitalar.mapper.ConsultaMapper;
import com.gerenciamento_hospitalar.mapper.PacienteMapper;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.model.Usuario;
import com.gerenciamento_hospitalar.repository.PacienteRepository;
import com.gerenciamento_hospitalar.repository.UsuarioRepository;
import com.gerenciamento_hospitalar.service.PacienteService;
import com.gerenciamento_hospitalar.service.SecurityService;
import com.gerenciamento_hospitalar.validator.PacienteValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PacienteValidator validator;

    @Mock
    private PacienteImporterFactory factory;

    @Mock
    private PacienteMapper mapper;

    @Mock
    private ConsultaMapper consultaMapper;

    @Mock
    private SecurityService securityService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PacienteService service;

    private Paciente paciente;
    private Paciente pacienteSalvo;
    private PacienteRequest request;
    private PacienteResponse response;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        paciente = mockPaciente();
        pacienteSalvo = mockPacienteSalvo();
        request = mockPacienteRequest();
        response = mockPacienteResponse();
        usuario = mockUsuario();
    }

    @Test
    void addPaciente() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(paciente);
        when(usuarioRepository.findById(request.userId())).thenReturn(Optional.of(usuario));
        doNothing().when(validator).validar(paciente);
        when(pacienteRepository.save(paciente)).thenReturn(pacienteSalvo);
        when(mapper.toDTO(pacienteSalvo)).thenReturn(response);

        // 2.execução
        var resultado = service.addPaciente(request);

        // 3.verificação
        assertNotNull(resultado.id());
        assertTrue(resultado.id() > 0);

        assertEquals(resultado.cpf(), response.cpf());
        assertEquals(resultado.email(), response.email());
        assertEquals(resultado.endereco(), response.endereco());
        assertEquals(resultado.telefone(), response.telefone());
        assertEquals(resultado.genero(), response.genero());
        assertEquals(resultado.tipoSanguineo(), response.tipoSanguineo());
        assertEquals(resultado.dataNascimento(), response.dataNascimento());

        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    void atualizarPaciente() {
        // 1.cenário
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteSalvo));
        doNothing().when(validator).validar(pacienteSalvo);
        when(pacienteRepository.save(pacienteSalvo)).thenReturn(pacienteSalvo);
        when(mapper.toDTO(pacienteSalvo)).thenReturn(response);

        // 2.execução
        var resultado = service.atualizarPaciente(1L, request);

        // 3.verificação
        assertNotNull(resultado.id());
        assertTrue(resultado.id() > 0);

        assertEquals(resultado.cpf(), response.cpf());
        assertEquals(resultado.email(), response.email());
        assertEquals(resultado.endereco(), response.endereco());
        assertEquals(resultado.telefone(), response.telefone());
        assertEquals(resultado.genero(), response.genero());
        assertEquals(resultado.tipoSanguineo(), response.tipoSanguineo());
        assertEquals(resultado.dataNascimento(), response.dataNascimento());

        verify(pacienteRepository, times(1)).save(pacienteSalvo);
    }

    @Test
    void obterPacientePeloId() {
        // 1.cenário
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteSalvo));
        doNothing().when(securityService).validaUsuarioPaciente(pacienteSalvo);
        when(mapper.toDTO(pacienteSalvo)).thenReturn(response);

        // 2.execução
        var resultado = service.obterPacientePeloId(1L);

        // 3.verificação
        assertNotNull(resultado.id());
        assertTrue(resultado.id() > 0);

        assertEquals(resultado.cpf(), response.cpf());
        assertEquals(resultado.email(), response.email());
        assertEquals(resultado.endereco(), response.endereco());
        assertEquals(resultado.telefone(), response.telefone());
        assertEquals(resultado.genero(), response.genero());
        assertEquals(resultado.tipoSanguineo(), response.tipoSanguineo());
        assertEquals(resultado.dataNascimento(), response.dataNascimento());
    }

    @Test
    void deletarPaciente() {
        // 1.cenário
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteSalvo));
        doNothing().when(validator).validarDelecao(pacienteSalvo);
        doNothing().when(pacienteRepository).delete(pacienteSalvo);

        // 2.execução e verificação
        assertDoesNotThrow(() -> service.deletarPaciente(1L));
        verify(pacienteRepository, times(1)).delete(any(Paciente.class));

    }

    @Test
    void addPacienteComCpfDuplicado() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(paciente);
        when(usuarioRepository.findById(request.userId())).thenReturn(Optional.of(usuario));
        doThrow(RegistroDuplicadoException.class).when(validator).validar(paciente);

        // 2.execução
        var resultado = catchThrowable(() -> service.addPaciente(request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);

        verify(pacienteRepository, times(0)).save(paciente);
    }


    @Test
    void addPacienteComUsuarioInexistente() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(paciente);
        doThrow(RegistroNaoEncontradoException.class).when(usuarioRepository).findById(request.userId());

        // 2.execução
        var resultado = catchThrowable(() -> service.addPaciente(request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);

        verify(pacienteRepository, times(0)).save(paciente);
    }


    @Test
    void atualizarPacienteInexistente() {
        // 1.cenário
        doThrow(RegistroNaoEncontradoException.class).when(pacienteRepository).findById(1L);

        // 2.execução
        var resultado = catchThrowable(() -> service.atualizarPaciente(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);

        verify(pacienteRepository, times(0)).save(pacienteSalvo);
    }


    @Test
    void atualizarPacienteComCpfDuplicado() {
        // 1.cenário
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteSalvo));
        doThrow(RegistroDuplicadoException.class).when(validator).validar(pacienteSalvo);

        // 2.execução
        var resultado = catchThrowable(() -> service.atualizarPaciente(1L, request));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);

        verify(pacienteRepository, times(0)).save(paciente);
    }


    @Test
    void obterPacienteInexistente() {
        // 1.cenário
        doThrow(RegistroNaoEncontradoException.class).when(pacienteRepository).findById(1L);

        // 2.execução
        var resultado = catchThrowable(() -> service.obterPacientePeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);

        verify(mapper, times(0)).toDTO(any());
    }

    @Test
    void pacienteTentandoObterDadosDeOutroPaciente() {
        // 1.cenário
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteSalvo));
        doThrow(AcessoNegadoException.class).when(securityService).validaUsuarioPaciente(pacienteSalvo);

        // 2.execução
        var resultado = catchThrowable(() -> service.obterPacientePeloId(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(AcessoNegadoException.class);

        verify(mapper, times(0)).toDTO(any());
    }


    @Test
    void deletarPacienteInexistente() {
        // 1.cenário
        doThrow(RegistroNaoEncontradoException.class).when(pacienteRepository).findById(1L);

        // 2.execução
        var resultado = catchThrowable(() -> service.deletarPaciente(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class);

        verify(pacienteRepository, times(0)).delete(any(Paciente.class));
    }


    @Test
    void deletarPacienteComConsultasRegistradasNoBanco() {
        // 1.cenário
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteSalvo));
        doThrow(DelecaoNaoPermitidaException.class).when(validator).validarDelecao(pacienteSalvo);

        // 2.execução e verificação
        var resultado = catchThrowable(() -> service.deletarPaciente(1L));

        // 3.verificação
        assertThat(resultado).isInstanceOf(DelecaoNaoPermitidaException.class);

        verify(pacienteRepository, times(0)).delete(any(Paciente.class));

    }

    // mocks
    private Paciente mockPaciente() {
        Paciente paciente = new Paciente();
        paciente.setCpf("745.605.880-66");
        paciente.setNome("paciente1");
        paciente.setEmail("paciente@email.com");
        paciente.setEndereco("endereço1");
        paciente.setGenero("masculino");
        paciente.setTelefone("Telefone");
        paciente.setTipoSanguineo("O-");
        paciente.setDataNascimento(LocalDate.of(1994, 2, 10));
        paciente.setUsuario(null);

        return paciente;
    }

    private Paciente mockPacienteSalvo() {
        Paciente paciente = mockPaciente();
        paciente.setId(1L);
        return paciente;
    }

    private PacienteRequest mockPacienteRequest() {
        String cpf = "745.605.880-66";
        String nome = "paciente1";
        String email = "paciente@email.com";
        String genero = "masculino";
        String telefone = "Telefone";
        String tipoSanguineo = "O-";
        String endereco = "endereço1";
        LocalDate dataNascimento = LocalDate.of(1994, 2, 10);

        PacienteRequest request = new PacienteRequest(cpf, nome, genero, endereco, email, telefone, tipoSanguineo, dataNascimento, 1L);

        return request;
    }

    private PacienteResponse mockPacienteResponse() {
        String cpf = "745.605.880-66";
        String nome = "paciente1";
        String email = "paciente@email.com";
        String genero = "masculino";
        String telefone = "Telefone";
        String tipoSanguineo = "O-";
        String endereco = "endereço1";
        LocalDate dataNascimento = LocalDate.of(1994, 2, 10);

        PacienteResponse response = new PacienteResponse(1L, cpf, nome, genero, endereco, email, telefone, tipoSanguineo, dataNascimento);

        return response;
    }

    private Usuario mockUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNomeCompleto("nome1");
        usuario.setUsername("username1");
        usuario.setSenha("123");
        usuario.setEnabled(true);
        usuario.setCredentialsNonExpired(true);
        usuario.setAccountNonLocked(true);
        usuario.setAccountNonExpired(true);

        return usuario;
    }

}