package com.gerenciamento_hospitalar.validator;

import com.gerenciamento_hospitalar.exception.ConsultasConflitantesException;
import com.gerenciamento_hospitalar.exception.HoraForaDoPadraoException;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConsultaValidator {

    private final ConsultaRepository consultaRepository;

    public void validar(Consulta consulta) {
        if(datasConflitantes(consulta)) {
            throw new ConsultasConflitantesException("");
        }

        if(horarioForaDoPadrao(consulta)) {
            throw new HoraForaDoPadraoException("Horário da consulta não segue o padrão especificado!");
        }
    }

    public boolean datasConflitantes(Consulta consulta) {
        Optional<Consulta> consultaEncontrada = consultaRepository
                .findByMedicoAndDataAndHora(consulta.getMedico(), consulta.getData(), consulta.getHora());

        if(consulta.getId() == null) {
            return consultaEncontrada.isPresent();
        }

        return consultaEncontrada.map(Consulta::getId)
                .stream()
                .anyMatch(id -> !id.equals(consulta.getId()));
    }

    public boolean horarioForaDoPadrao(Consulta consulta) {
        int minuto = consulta.getHora().getMinute();

        if(minuto % 20 != 0) {
            return true;
        }

        return false;
    }
}
