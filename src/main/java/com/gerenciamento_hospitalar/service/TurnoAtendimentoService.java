package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.TurnoAtendimentoRequest;
import com.gerenciamento_hospitalar.dto.response.TurnoAtendimentoResponse;
import com.gerenciamento_hospitalar.exception.AcessoNegadoException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.TurnoAtendimentoMapper;
import com.gerenciamento_hospitalar.model.TurnoAtendimento;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.model.Usuario;
import com.gerenciamento_hospitalar.repository.TurnoAtendimentoRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.validator.TurnoAtendimentoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TurnoAtendimentoService {

    private final TurnoAtendimentoRepository turnoAtendimentoRepository;
    private final TurnoAtendimentoValidator validator;
    private final MedicoRepository medicoRepository;
    private final SecurityService securityService;
    private final TurnoAtendimentoMapper mapper;


    public TurnoAtendimentoResponse addDisponibilidadeMedico(Long id, TurnoAtendimentoRequest request) {
        TurnoAtendimento turnoAtendimento = mapper.toEntity(request);

        Medico medico = obterMedicoPorIdOuLancarExcecao(id);

        turnoAtendimento.setMedico(medico);

        validator.validar(turnoAtendimento);
        return mapper.toDTO(turnoAtendimentoRepository.save(turnoAtendimento));
    }


    public TurnoAtendimentoResponse atualizarDisponibilidadeMedico(Long turnoId, TurnoAtendimentoRequest request) {

        TurnoAtendimento turno = obterTurnoPorIdOuLancarExcecao(turnoId);

        turno.setDiaSemana(request.diaSemana());
        turno.setHoraInicio(request.horaInicio());
        turno.setHoraFim(request.horaFim());

        validator.validar(turno);
        return mapper.toDTO(turnoAtendimentoRepository.save(turno));
    }

    @Transactional
    public List<TurnoAtendimentoResponse> obterTurnosDeMedicoPeloId(Long id, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);

        Medico medico = obterMedicoPorIdOuLancarExcecao(id);

        List<TurnoAtendimento> disponibilidades = medico.getDisponibilidades();

        if(disponibilidades == null || disponibilidades.isEmpty()) {
            return List.of();
        }

        return disponibilidades.stream().map(mapper::toDTO).toList();
    }

    public void deletarTurnoPeloId(Long id) {
        TurnoAtendimento turno = obterTurnoPorIdOuLancarExcecao(id);
        validator.validarDelecao(turno);
        turnoAtendimentoRepository.delete(turno);
    }

    @Transactional
    public List<TurnoAtendimentoResponse> obterTurnosDeMedicoLogado() {
        Usuario usuario = securityService.getUsuarioLogado();
        if(!usuario.getRoles().contains("MEDICO")) {
            throw new AcessoNegadoException("acesso negado!");
        }

        Optional<Medico> medicoOpt = medicoRepository.findByUsuario(usuario);

        if(medicoOpt.isPresent()) {
            Medico medico = medicoOpt.get();
            List<TurnoAtendimento> turnos = medico.getDisponibilidades();
            if (turnos == null) return List.of();

            return turnos.stream().map(mapper::toDTO).toList();
        }

        throw new AcessoNegadoException("acesso negado!");
    }

    private TurnoAtendimento obterTurnoPorIdOuLancarExcecao(Long id) {
        return turnoAtendimentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe disponibilidade com esse ID!"));
    }

    private Medico obterMedicoPorIdOuLancarExcecao(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe médico com esse ID!"));
    }
}
