package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.DisponibilidadeMedicoRequest;
import com.gerenciamento_hospitalar.dto.response.DisponibilidadeMedicoResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.DisponibilidadeMedicoMapper;
import com.gerenciamento_hospitalar.model.DisponibilidadeMedico;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.repository.DisponibilidadeMedicoRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.validator.DisponibilidadeMedicoValidator;
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
public class DisponibilidadeMedicoService {

    private final DisponibilidadeMedicoRepository disponibilidadeMedicoRepository;
    private final DisponibilidadeMedicoValidator validator;
    private final MedicoRepository medicoRepository;
    private final DisponibilidadeMedicoMapper mapper;

    public DisponibilidadeMedicoResponse addDisponibilidadeMedico2(DisponibilidadeMedicoRequest request) {
        DisponibilidadeMedico disponibilidadeMedico = mapper.toEntity(request);

        Medico medico = obterMedicoPorIdOuLancarExcecao(request.medicoId());

        disponibilidadeMedico.setMedico(medico);

        validator.validar(disponibilidadeMedico);
        return mapper.toDTO(disponibilidadeMedicoRepository.save(disponibilidadeMedico));
    }

    public DisponibilidadeMedicoResponse addDisponibilidadeMedico(Long id, DisponibilidadeMedicoRequest request) {
        DisponibilidadeMedico disponibilidadeMedico = mapper.toEntity(request);

        Medico medico = obterMedicoPorIdOuLancarExcecao(id);

        disponibilidadeMedico.setMedico(medico);

        validator.validar(disponibilidadeMedico);
        return mapper.toDTO(disponibilidadeMedicoRepository.save(disponibilidadeMedico));
    }



    public DisponibilidadeMedicoResponse atualizarDisponibilidadeMedico2(Long id, DisponibilidadeMedicoRequest request) {
        DisponibilidadeMedico disponibilidadeMedico = obterPorIdOuLancarExcecao(id);

        disponibilidadeMedico.setDiaSemana(request.diaSemana());
        disponibilidadeMedico.setHoraInicio(request.horaInicio());
        disponibilidadeMedico.setHoraFim(request.horaFim());

        Medico medico = obterMedicoPorIdOuLancarExcecao(request.medicoId());

        disponibilidadeMedico.setMedico(medico);

        validator.validar(disponibilidadeMedico);
        return mapper.toDTO(disponibilidadeMedicoRepository.save(disponibilidadeMedico));
    }

    @Transactional
    public DisponibilidadeMedicoResponse atualizarDisponibilidadeMedico(Long medicoId,
                                                                        Long disponibilidadeId,
                                                                        DisponibilidadeMedicoRequest request) {

        Medico medico = obterMedicoPorIdOuLancarExcecao(medicoId);
        List<DisponibilidadeMedico> disponibilidades = medico.getDisponibilidades();

        Optional<DisponibilidadeMedico> disponibilidadeOpt = disponibilidades
                .stream()
                .filter(disponibilidadeMedico -> disponibilidadeMedico.getId().equals(disponibilidadeId))
                .findFirst();

        if(disponibilidadeOpt.isPresent()) {
            DisponibilidadeMedico disponibilidade = disponibilidadeOpt.get();
            disponibilidade.setDiaSemana(request.diaSemana());
            disponibilidade.setHoraInicio(request.horaInicio());
            disponibilidade.setHoraFim(request.horaFim());
            validator.validar(disponibilidade);
            return mapper.toDTO(disponibilidadeMedicoRepository.save(disponibilidade));
        }

        throw new RuntimeException("Não existe disponibilidade com esse ID!");
    }



    public DisponibilidadeMedicoResponse obterPeloId(Long id) {
        return mapper.toDTO(obterPorIdOuLancarExcecao(id));
    }

    public Page<DisponibilidadeMedicoResponse> listarDisponibilidades2(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);

        return disponibilidadeMedicoRepository.findAll(pageable).map(mapper::toDTO);
    }

    @Transactional
    public List<DisponibilidadeMedicoResponse> listarDisponibilidades(Long id, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);

        Medico medico = obterMedicoPorIdOuLancarExcecao(id);
        List<DisponibilidadeMedico> disponibilidades = medico.getDisponibilidades();

        return disponibilidades.stream().map(mapper::toDTO).toList();
    }



    public void deletarPeloId(Long id) {
        DisponibilidadeMedico disponibilidadeMedico = obterPorIdOuLancarExcecao(id);
        validator.validarDelecao(disponibilidadeMedico);
        disponibilidadeMedicoRepository.delete(disponibilidadeMedico);
    }

    @Transactional
    public void deletarPeloIdMedico(Long MedicoId, Long disponibilidadeMedicoId) {
        Medico medico = obterMedicoPorIdOuLancarExcecao(MedicoId);
        List<DisponibilidadeMedico> disponibilidades = medico.getDisponibilidades();

        Optional<DisponibilidadeMedico> disponibilidadeOpt = disponibilidades.stream()
                        .filter(disponibilidadeMed -> disponibilidadeMed.getId().equals(disponibilidadeMedicoId))
                        .findFirst();

        if(disponibilidadeOpt.isPresent()) {
            DisponibilidadeMedico disponibilidade = disponibilidadeOpt.get();
            validator.validarDelecao(disponibilidade);
            disponibilidadeMedicoRepository.delete(disponibilidade);
        }

        throw new RuntimeException("Não existe disponibilidade com esse ID!");

    }



    private DisponibilidadeMedico obterPorIdOuLancarExcecao(Long id) {
        return disponibilidadeMedicoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe disponibilidade com esse ID!"));
    }

    private Medico obterMedicoPorIdOuLancarExcecao(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe médico com esse ID!"));
    }
}
