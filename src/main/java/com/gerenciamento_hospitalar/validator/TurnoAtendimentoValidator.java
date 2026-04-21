package com.gerenciamento_hospitalar.validator;

import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.HorariosConflitantesException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.model.DiaSemana;
import com.gerenciamento_hospitalar.model.TurnoAtendimento;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.TurnoAtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class TurnoAtendimentoValidator {

    private final TurnoAtendimentoRepository turnoAtendimentoRepository;
    private final ConsultaRepository consultaRepository;

    public void validar(TurnoAtendimento turnoAtendimento) {
        if(verificaSobreposicao(turnoAtendimento)) {
            throw new HorariosConflitantesException("Conflito de disponibilidade: o horário informado se sobrepõe a outro já cadastrado para o médico neste dia.");
        }

        if(turnoAtendimento.getHoraInicio().isAfter(turnoAtendimento.getHoraFim())) {
            throw new RuntimeException("hora de inicio não pode ser posterior a hora de fim");
        }
    }

    private boolean verificaSobreposicao(TurnoAtendimento turnoAtendimento) {
        Medico medico = turnoAtendimento.getMedico();
        DiaSemana diaSemana = turnoAtendimento.getDiaSemana();
        LocalTime horaInicio = turnoAtendimento.getHoraInicio();
        LocalTime horaFim = turnoAtendimento.getHoraFim();

        return turnoAtendimentoRepository
                .verificaSobreposicaoDeTurnos(medico, diaSemana, horaInicio, horaFim);
    }

    public void validarDelecao(TurnoAtendimento turnoAtendimento) {
        Medico medico = turnoAtendimento.getMedico();
        DiaSemana diaSemana = turnoAtendimento.getDiaSemana();
        LocalTime horaInicio = turnoAtendimento.getHoraInicio();
        LocalTime horaFim = turnoAtendimento.getHoraFim();

        if(consultaRepository.existeConsulta(medico, diaSemana, horaInicio, horaFim)) {
            throw new DelecaoNaoPermitidaException("Disponibilidade não pode ser excluída pois há consultas agendadas para o médico neste dia e horário.");
        }
    }
}
