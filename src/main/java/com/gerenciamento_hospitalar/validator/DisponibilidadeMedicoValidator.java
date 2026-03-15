package com.gerenciamento_hospitalar.validator;

import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.DisponibilidadeMedico;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.DisponibilidadeMedicoRepository;
import com.gerenciamento_hospitalar.repository.specs.ConsultaSpecs;
import com.gerenciamento_hospitalar.repository.specs.DisponibilidadeMedicoSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisponibilidadeMedicoValidator {

    private final DisponibilidadeMedicoRepository disponibilidadeMedicoRepository;
    private final ConsultaRepository consultaRepository;

    public void validar(DisponibilidadeMedico disponibilidadeMedico) {
        if(verificaSobreposicao(disponibilidadeMedico)) {
            throw new RuntimeException("sdsadsad");
        }
    }

    private boolean verificaSobreposicao(DisponibilidadeMedico disponibilidadeMedico) {

        Specification<DisponibilidadeMedico> specs = DisponibilidadeMedicoSpecs
                .verificaSobreposicao(disponibilidadeMedico.getMedico(), disponibilidadeMedico.getDiaSemana(),
                        disponibilidadeMedico.getHoraInicio(), disponibilidadeMedico.getHoraFim());

        return disponibilidadeMedicoRepository.exists(specs);
    }

    public void validarDelecao(DisponibilidadeMedico disponibilidadeMedico) {
        Specification<Consulta> specs = ConsultaSpecs
                .verificaDelecao(disponibilidadeMedico.getMedico(), disponibilidadeMedico.getDiaSemana(),
                        disponibilidadeMedico.getHoraInicio(), disponibilidadeMedico.getHoraFim());

        if(consultaRepository.exists(specs)) {
            throw new DelecaoNaoPermitidaException("Deleção não permitida");
        }
    }
}
