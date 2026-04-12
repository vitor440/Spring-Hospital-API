package com.gerenciamento_hospitalar.validator;

import com.gerenciamento_hospitalar.exception.HorariosConflitantesException;
import com.gerenciamento_hospitalar.exception.HoraForaDoPadraoException;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.TurnoAtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConsultaValidator {

    private final ConsultaRepository consultaRepository;
    private final TurnoAtendimentoRepository turnoAtendimentoRepository;

    public void validar(Consulta consulta) {
        if(datasConflitantes(consulta)) {
            throw new HorariosConflitantesException("Já existe consulta nessa data e horario!");
        }

        if(horarioForaDoPadrao(consulta)) {
            throw new HoraForaDoPadraoException("Horário da consulta não segue o padrão especificado!");
        }

        if(!medicoIndisponivel(consulta)) {
            throw new HorariosConflitantesException("Médico não disponível!");
        }
    }

    private boolean datasConflitantes(Consulta consulta) {
        Optional<Consulta> consultaEncontrada = consultaRepository
                .findByMedicoAndDataAndHora(consulta.getMedico(), consulta.getData(), consulta.getHora());

        if(consulta.getId() == null) {
            return consultaEncontrada.isPresent();
        }

        return consultaEncontrada.map(Consulta::getId)
                .stream()
                .anyMatch(id -> !id.equals(consulta.getId()));
    }

    private boolean horarioForaDoPadrao(Consulta consulta) {
        int minuto = consulta.getHora().getMinute();

        if(minuto % 20 != 0) {
            return true;
        }

        return false;
    }

    private boolean medicoIndisponivel(Consulta consulta) {
        return turnoAtendimentoRepository
                .verificaDisponibilidadeMedico(consulta.getMedico(), consulta.getDiaSemana(), consulta.getHora());
    }
 }
