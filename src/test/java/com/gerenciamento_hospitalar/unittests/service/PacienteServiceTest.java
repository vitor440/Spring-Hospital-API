package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.file.imports.factory.PacienteImporterFactory;
import com.gerenciamento_hospitalar.mapper.PacienteMapper;
import com.gerenciamento_hospitalar.mocks.PacienteMock;
import com.gerenciamento_hospitalar.mocks.UsuarioMock;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.model.Usuario;
import com.gerenciamento_hospitalar.repository.PacienteRepository;
import com.gerenciamento_hospitalar.repository.UsuarioRepository;
import com.gerenciamento_hospitalar.service.PacienteService;
import com.gerenciamento_hospitalar.service.SecurityService;
import com.gerenciamento_hospitalar.validator.PacienteValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private SecurityService securityService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PacienteService service;

    private Paciente paciente;
    private PacienteRequest request;
    private PacienteResponse response;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Assumindo que você tem essas classes de Mock seguindo o padrão das anteriores
        paciente = PacienteMock.mockPaciente(1);
        request = PacienteMock.mockPacienteRequest(1);
        response = PacienteMock.mockPacienteResponse(1);
        usuario = UsuarioMock.mockUsuario(1);
    }


    // === 1. ADICIONAR PACIENTES ===
    @Test
    @DisplayName("Deve adicionar um paciente com sucesso")
    void addPaciente() {
        // 1. Cenário
        when(mapper.toEntity(request)).thenReturn(paciente);
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(pacienteRepository.save(paciente)).thenReturn(paciente);
        when(mapper.toDTO(paciente)).thenReturn(response);

        // 2. Execução
        var resultado = service.addPaciente(request);

        // 3. Verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(validator, times(1)).validar(paciente);
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar um paciente com usuario inexistente no banco")
    void addPacienteComUsuarioInexistente() {
        // 1. Cenário
        when(mapper.toEntity(request)).thenReturn(paciente);
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // 2. Execução
        var resultado = catchThrowable(() -> service.addPaciente(request));

        // 3. Verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe usuario com esse ID!");
        verify(validator, times(0)).validar(paciente);
        verify(pacienteRepository, times(0)).save(paciente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar um paciente com cpf duplicado")
    void addPacienteComCpfDuplicado() {
        // 1. Cenário
        when(mapper.toEntity(request)).thenReturn(paciente);
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        doThrow(RegistroDuplicadoException.class).when(validator).validar(paciente);

        // 2. Execução
        var resultado = catchThrowable(() -> service.addPaciente(request));

        // 3. Verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);
        verify(validator, times(1)).validar(paciente);
        verify(pacienteRepository, times(0)).save(paciente);
    }


    // === 2. ATUALIZAR PACIENTES ===
    @Test
    @DisplayName("Deve atualizar um paciente com sucesso")
    void atualizarPaciente() {
        // 1. Cenário
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(pacienteRepository.save(paciente)).thenReturn(paciente);
        when(mapper.toDTO(paciente)).thenReturn(response);

        // 2. Execução
        var resultado = service.atualizarPaciente(id, request);

        // 3. Verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(validator, times(1)).validar(paciente);
        verify(pacienteRepository, times(1)).save(paciente);

    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar um paciente inexistente no banco")
    void atualizarPacienteInexistente() {
        // 1. Cenário
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.empty());

        // 2. Execução
        var resultado = catchThrowable(() -> service.atualizarPaciente(id, request));

        // 3. Verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe paciente com esse ID!");
        verify(validator, times(0)).validar(paciente);
        verify(pacienteRepository, times(0)).save(paciente);

    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar um paciente com usuario inexistente no banco")
    void atualizarPacienteComUsuarioInexistente() {
        // 1. Cenário
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // 2. Execução
        var resultado = catchThrowable(() -> service.atualizarPaciente(id, request));

        // 3. Verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe usuario com esse ID!");
        verify(validator, times(0)).validar(paciente);
        verify(pacienteRepository, times(0)).save(paciente);

    }

    @Test
    @DisplayName("Deve atualizar um paciente com sucesso")
    void atualizarPacienteComCpfDuplicado() {
        // 1. Cenário
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        doThrow(RegistroDuplicadoException.class).when(validator).validar(paciente);

        // 2. Execução
        var resultado = catchThrowable(() -> service.atualizarPaciente(id, request));

        // 3. Verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);
        verify(validator, times(1)).validar(paciente);
        verify(pacienteRepository, times(0)).save(paciente);

    }

    // === 3. OBTER PACIENTES ===
    @Test
    @DisplayName("Deve obter um paciente pelo ID com sucesso")
    void obterPacientePeloId() {
        // 1. Cenário
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(mapper.toDTO(paciente)).thenReturn(response);

        // 2. Execução
        var resultado = service.obterPacientePeloId(id);

        // 3. Verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(pacienteRepository, times(1)).findById(id);
        verify(mapper, times(1)).toDTO(paciente);
    }

    @Test
    @DisplayName("Deve obter um paciente pelo ID com sucesso")
    void obterPacienteInexistente() {
        // 1. Cenário
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.empty());

        // 2. Execução
        var resultado = catchThrowable(() -> service.obterPacientePeloId(id));

        // 3. Verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe paciente com esse ID!");
        verify(pacienteRepository, times(1)).findById(id);
        verify(mapper, times(0)).toDTO(paciente);
    }


    // === 4. DELETAR PACIENTES ===
    @Test
    @DisplayName("Deve deletar um paciente com sucesso")
    void deletarPaciente() {
        // 1. Cenário
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));

        // 2. Execução
        service.deletarPaciente(id);

        // 3. Verificação
        verify(validator, times(1)).validarDelecao(paciente);
        verify(pacienteRepository, times(1)).delete(paciente);
    }

    @Test
    @DisplayName("Deve deletar um paciente com sucesso")
    void deletarPacienteComConsultasRegistradas() {
        // 1. Cenário
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        doThrow(DelecaoNaoPermitidaException.class).when(validator).validarDelecao(paciente);

        // 2. Execução
        var resultado = catchThrowable(() -> service.deletarPaciente(id));

        // 3. Verificação
        assertThat(resultado).isInstanceOf(DelecaoNaoPermitidaException.class);
        verify(validator, times(1)).validarDelecao(paciente);
        verify(pacienteRepository, times(0)).delete(paciente);
    }



    @Test
    @DisplayName("Deve deletar um paciente com sucesso")
    void deletarPacienteInexistente() {
        // 1. Cenário
        Long id = 1L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.empty());

        // 2. Execução
        var resultado = catchThrowable(() -> service.deletarPaciente(id));

        // 3. Verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe paciente com esse ID!");
        verify(validator, times(0)).validarDelecao(paciente);
        verify(pacienteRepository, times(0)).delete(paciente);
    }


}