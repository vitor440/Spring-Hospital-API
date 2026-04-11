package com.gerenciamento_hospitalar.unittests.service;

import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.exception.AcessoNegadoException;
import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.file.imports.factory.MedicoImporterFactory;
import com.gerenciamento_hospitalar.mapper.MedicoMapper;
import com.gerenciamento_hospitalar.mocks.DepartamentoMock;
import com.gerenciamento_hospitalar.mocks.MedicoMock;
import com.gerenciamento_hospitalar.mocks.UsuarioMock;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.model.Usuario;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private MedicoValidator validator;

    @Mock
    private MedicoMapper mapper;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private MedicoImporterFactory factory;

    @Mock
    private SecurityService securityService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MedicoService service;

    private Medico medico;
    private MedicoRequest request;
    private MedicoResponse response;
    private Departamento departamento;
    private Usuario usuario;


    @BeforeEach
    void setUp() {
        medico = MedicoMock.mockMedico(1);
        request = MedicoMock.mockRequest(1);
        response = MedicoMock.mockResponse(1);
        usuario = UsuarioMock.mockUsuario(1);
        departamento = DepartamentoMock.mockDepartamento(1);
    }

    @Test
    void addMedico() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(medico);
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(medicoRepository.save(medico)).thenReturn(medico);
        when(mapper.toDTO(medico)).thenReturn(response);

        // 2.execução
        var resultado = service.addMedico(request);

        // 3. verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(validator, times(1)).validar(medico);
        verify(medicoRepository, times(1)).save(medico);
        verify(mapper, times(1)).toDTO(medico);
    }

    @Test
    void addMedicoComDepartamentoInexistente() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(medico);
        when(departamentoRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.addMedico(request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe departamento com esse ID!");
        verify(validator, times(0)).validar(medico);
        verify(medicoRepository, times(0)).save(medico);
        verify(mapper, times(0)).toDTO(medico);
    }

    @Test
    void addMedicoComUsuarioInexistente() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(medico);
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.addMedico(request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe usuario com esse ID!");
        verify(validator, times(0)).validar(medico);
        verify(medicoRepository, times(0)).save(medico);
        verify(mapper, times(0)).toDTO(medico);
    }

    @Test
    void addMedicoComCrmDuplicado() {
        // 1.cenário
        when(mapper.toEntity(request)).thenReturn(medico);
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doThrow(RegistroDuplicadoException.class).when(validator).validar(medico);

        // 2.execução
        var resultado = catchThrowable(() -> service.addMedico(request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);
        verify(validator, times(1)).validar(medico);
        verify(medicoRepository, times(0)).save(medico);
        verify(mapper, times(0)).toDTO(medico);
    }

    @Test
    void atualizarMedico() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(medicoRepository.save(medico)).thenReturn(medico);
        when(mapper.toDTO(medico)).thenReturn(response);

        // 2.execução
        var resultado = service.atualizarMedico(1L, request);

        // 3. verificação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(validator, times(1)).validar(medico);
        verify(medicoRepository, times(1)).save(medico);
        verify(mapper, times(1)).toDTO(medico);
    }

    @Test
    void atualizarMedicoComDepartamentoInexistente() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(departamentoRepository.findById(1L)).thenReturn(Optional.empty());


        // 2.execução
        var resultado = catchThrowable(() -> service.atualizarMedico(1L, request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe departamento com esse ID!");
        verify(validator, times(0)).validar(medico);
        verify(medicoRepository, times(0)).save(medico);
        verify(mapper, times(0)).toDTO(medico);
    }

    @Test
    void atualizarMedicoComCrmDuplicado() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doThrow(RegistroDuplicadoException.class).when(validator).validar(medico);

        // 2.execução
        var resultado = catchThrowable(() -> service.atualizarMedico(1L, request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroDuplicadoException.class);
        verify(validator, times(1)).validar(medico);
        verify(medicoRepository, times(0)).save(medico);
        verify(mapper, times(0)).toDTO(medico);
    }

    @Test
    void atualizarMedicoComUsuarioInexistente() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.atualizarMedico(1L, request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe usuario com esse ID!");
        verify(validator, times(0)).validar(medico);
        verify(medicoRepository, times(0)).save(medico);
        verify(mapper, times(0)).toDTO(medico);
    }

    @Test
    void atualizarMedicoInexistente() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());


        // 2.execução
        var resultado = catchThrowable(() -> service.atualizarMedico(1L, request));

        // 3. verificação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe médico com esse ID!");
        verify(validator, times(0)).validar(medico);
        verify(medicoRepository, times(0)).save(medico);
        verify(mapper, times(0)).toDTO(medico);
    }

    @Test
    void obterMedicoPeloId() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(mapper.toDTO(medico)).thenReturn(response);

        // 2.execução
        var resultado = service.obterMedicoPeloId(1L);

        // 3. verficação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(mapper, times(1)).toDTO(medico);
    }

    @Test
    void obterMedicoInexistente() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.obterMedicoPeloId(1L));

        // 3. verficação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe médico com esse ID!");
        verify(mapper, times(0)).toDTO(medico);
    }



    @Test
    void obterDadosMedicoLogado() {
        // 1.cenário
        when(securityService.getUsuarioLogado()).thenReturn(usuario);
        when(medicoRepository.findByUsuario(usuario)).thenReturn(Optional.empty());


        // 2.execução
        var resultado = catchThrowable(() -> service.obterMedicoLogado());

        // 3. verficação
        assertThat(resultado).isInstanceOf(AcessoNegadoException.class).hasMessage("acesso negado.");
        verify(mapper, times(0)).toDTO(medico);
    }

    @Test
    void usuarioMedicoObtendoDadosDeOutroUsuario() {
        // 1.cenário
        when(securityService.getUsuarioLogado()).thenReturn(usuario);
        when(medicoRepository.findByUsuario(usuario)).thenReturn(Optional.of(medico));
        when(mapper.toDTO(medico)).thenReturn(response);

        // 2.execução
        var resultado = service.obterMedicoLogado();

        // 3. verficação
        assertThat(resultado).usingRecursiveComparison().isEqualTo(response);
        verify(mapper, times(1)).toDTO(medico);
    }



    @Test
    void deletarMedicoPeloId() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));

        // 2.execução
        service.deletarMedicoPeloId(1L);

        // 3. verficação
        verify(validator, times(1)).validarDelecao(medico);
        verify(medicoRepository, times(1)).delete(medico);
    }

    @Test
    void deletarMedicoInexistente() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // 2.execução
        var resultado = catchThrowable(() -> service.deletarMedicoPeloId(1L));

        // 3. verficação
        assertThat(resultado).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("Não existe médico com esse ID!");
        verify(validator, times(0)).validarDelecao(medico);
        verify(medicoRepository, times(0)).delete(medico);
    }

    @Test
    void deletarMedicoComConsultasRegistradas() {
        // 1.cenário
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        doThrow(DelecaoNaoPermitidaException.class).when(validator).validarDelecao(medico);

        // 2.execução
        var resultado = catchThrowable(() -> service.deletarMedicoPeloId(1L));

        // 3. verficação
        assertThat(resultado).isInstanceOf(DelecaoNaoPermitidaException.class);
        verify(validator, times(1)).validarDelecao(medico);
        verify(medicoRepository, times(0)).delete(medico);
    }
}