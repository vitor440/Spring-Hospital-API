package com.gerenciamento_hospitalar.repository.specs;

import com.gerenciamento_hospitalar.model.DisponibilidadeMedico;
import com.gerenciamento_hospitalar.model.Medico;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;

public class DisponibilidadeMedicoSpecs {

    public static Specification<DisponibilidadeMedico> verificaDisponibilidade(Medico medico, int diaSemana, LocalTime horario) {
        return (root, query, cb) ->
                cb.and(
                        cb.equal(root.get("medico"), medico),
                        cb.equal(root.get("diaSemana"), diaSemana),
                        cb.lessThanOrEqualTo(root.get("horaInicio"), horario),
                        cb.greaterThanOrEqualTo(root.get("horaFim"), horario));
    }

    public static Specification<DisponibilidadeMedico> verificaSobreposicao(Medico medico, int diaSemana, LocalTime horaInicio, LocalTime horaFim) {
        return (root, query, cb) ->
                cb.and(
                        cb.equal(root.get("medico"), medico),
                        cb.equal(root.get("diaSemana"), diaSemana),
                        cb.lessThanOrEqualTo(root.get("horaInicio"), horaFim),
                        cb.greaterThanOrEqualTo(root.get("horaFim"), horaInicio));
    }
}
