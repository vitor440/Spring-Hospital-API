package com.gerenciamento_hospitalar.repository.specs;

import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.Medico;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;

public class ConsultaSpecs {

    public static Specification<Consulta> verificaDelecao(Medico medico, int diaSemana, LocalTime horaInicio, LocalTime horaFim) {
        // SELECT EXIST WHERE medico = :medico AND (SELECT date_part('dow', data)) = :diaSemana AND hora BETWEEN :horaInicio AND :horaFim
        return (root, query, cb) ->
                cb.and(
                        cb.equal(root.get("medico"), medico),
                        cb.equal(root.get("diaSemana"), diaSemana),
                        cb.between(root.get("hora"), horaInicio, horaFim)
                );
    }
}
