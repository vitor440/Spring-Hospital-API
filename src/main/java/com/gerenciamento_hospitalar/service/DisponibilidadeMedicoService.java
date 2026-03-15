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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisponibilidadeMedicoService {

    private final DisponibilidadeMedicoRepository disponibilidadeMedicoRepository;
    private final DisponibilidadeMedicoValidator validator;
    private final MedicoRepository medicoRepository;
    private final DisponibilidadeMedicoMapper mapper;

    public DisponibilidadeMedicoResponse addDisponibilidadeMedico(DisponibilidadeMedicoRequest request) {
        DisponibilidadeMedico disponibilidadeMedico = mapper.toEntity(request);

        Medico medico = obterMedicoPorIdOuLancarExcecao(request.medicoId());

        disponibilidadeMedico.setMedico(medico);

        validator.validar(disponibilidadeMedico);
        return mapper.toDTO(disponibilidadeMedicoRepository.save(disponibilidadeMedico));
    }

    public DisponibilidadeMedicoResponse atualizarDisponibilidadeMedico(Long id, DisponibilidadeMedicoRequest request) {
        DisponibilidadeMedico disponibilidadeMedico = obterPorIdOuLancarExcecao(id);

        disponibilidadeMedico.setDiaSemana(request.diaSemana());
        disponibilidadeMedico.setHoraInicio(request.horaInicio());
        disponibilidadeMedico.setHoraFim(request.horaFim());

        Medico medico = obterMedicoPorIdOuLancarExcecao(request.medicoId());

        disponibilidadeMedico.setMedico(medico);

        validator.validar(disponibilidadeMedico);
        return mapper.toDTO(disponibilidadeMedicoRepository.save(disponibilidadeMedico));
    }

    public DisponibilidadeMedicoResponse obterPeloId(Long id) {
        return mapper.toDTO(obterPorIdOuLancarExcecao(id));
    }

    public Page<DisponibilidadeMedicoResponse> listarDisponibilidades(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);

        return disponibilidadeMedicoRepository.findAll(pageable).map(mapper::toDTO);
    }

    public void deletarPeloId(Long id) {
        DisponibilidadeMedico disponibilidadeMedico = obterPorIdOuLancarExcecao(id);
        validator.validarDelecao(disponibilidadeMedico);
        disponibilidadeMedicoRepository.delete(disponibilidadeMedico);
    }

    private DisponibilidadeMedico obterPorIdOuLancarExcecao(Long id) {
        return disponibilidadeMedicoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe registro com esse ID!"));
    }

    private Medico obterMedicoPorIdOuLancarExcecao(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe médico com esse ID!"));
    }
}
