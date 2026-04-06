package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.TurnoAtendimentoRequest;
import com.gerenciamento_hospitalar.dto.response.TurnoAtendimentoResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.TurnoAtendimentoMapper;
import com.gerenciamento_hospitalar.model.TurnoAtendimento;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.repository.TurnoAtendimentoRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.validator.TurnoAtendimentoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final TurnoAtendimentoMapper mapper;


    public TurnoAtendimentoResponse addDisponibilidadeMedico(Long id, TurnoAtendimentoRequest request) {
        TurnoAtendimento turnoAtendimento = mapper.toEntity(request);

        Medico medico = obterMedicoPorIdOuLancarExcecao(id);

        turnoAtendimento.setMedico(medico);

        validator.validar(turnoAtendimento);
        return mapper.toDTO(turnoAtendimentoRepository.save(turnoAtendimento));
    }


    @Transactional
    public TurnoAtendimentoResponse atualizarDisponibilidadeMedico(Long medicoId,
                                                                   Long disponibilidadeId,
                                                                   TurnoAtendimentoRequest request) {

        Medico medico = obterMedicoPorIdOuLancarExcecao(medicoId);
        List<TurnoAtendimento> disponibilidades = medico.getDisponibilidades();

        Optional<TurnoAtendimento> disponibilidadeOpt = disponibilidades
                .stream()
                .filter(disponibilidadeMedico -> disponibilidadeMedico.getId().equals(disponibilidadeId))
                .findFirst();

        if(disponibilidadeOpt.isPresent()) {
            TurnoAtendimento disponibilidade = disponibilidadeOpt.get();
            disponibilidade.setDiaSemana(request.diaSemana());
            disponibilidade.setHoraInicio(request.horaInicio());
            disponibilidade.setHoraFim(request.horaFim());
            validator.validar(disponibilidade);
            return mapper.toDTO(turnoAtendimentoRepository.save(disponibilidade));
        }

        throw new RuntimeException("Não existe disponibilidade com esse ID!");
    }



    public TurnoAtendimentoResponse obterPeloId(Long id) {
        return mapper.toDTO(obterTurnoPorIdOuLancarExcecao(id));
    }

    public Page<TurnoAtendimentoResponse> listarDisponibilidades2(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);

        return turnoAtendimentoRepository.findAll(pageable).map(mapper::toDTO);
    }

    @Transactional
    public List<TurnoAtendimentoResponse> listarDisponibilidades(Long id, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);

        Medico medico = obterMedicoPorIdOuLancarExcecao(id);
        List<TurnoAtendimento> disponibilidades = medico.getDisponibilidades();

        return disponibilidades.stream().map(mapper::toDTO).toList();
    }



    public void deletarPeloId(Long id) {
        TurnoAtendimento turnoAtendimento = obterTurnoPorIdOuLancarExcecao(id);
        validator.validarDelecao(turnoAtendimento);
        turnoAtendimentoRepository.delete(turnoAtendimento);
    }

    @Transactional
    public void deletarPeloIdMedico(Long MedicoId, Long disponibilidadeMedicoId) {
        Medico medico = obterMedicoPorIdOuLancarExcecao(MedicoId);
        List<TurnoAtendimento> disponibilidades = medico.getDisponibilidades();

        Optional<TurnoAtendimento> disponibilidadeOpt = disponibilidades.stream()
                        .filter(disponibilidadeMed -> disponibilidadeMed.getId().equals(disponibilidadeMedicoId))
                        .findFirst();

        if(disponibilidadeOpt.isPresent()) {
            TurnoAtendimento disponibilidade = disponibilidadeOpt.get();
            validator.validarDelecao(disponibilidade);
            turnoAtendimentoRepository.delete(disponibilidade);
        }

        throw new RuntimeException("Não existe disponibilidade com esse ID!");

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
