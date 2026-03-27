package com.gerenciamento_hospitalar.validator;

import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.model.DisponibilidadeMedico;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.DisponibilidadeMedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DisponibilidadeMedicoValidator {

    private final DisponibilidadeMedicoRepository disponibilidadeMedicoRepository;
    private final ConsultaRepository consultaRepository;

    public void validar(DisponibilidadeMedico disponibilidadeMedico) {
        if(verificaSobreposicao(disponibilidadeMedico)) {
            throw new RegistroDuplicadoException("Conflito de disponibilidade: o horário informado se sobrepõe a outro já cadastrado para o médico neste dia.");
        }
    }

    private boolean verificaSobreposicao(DisponibilidadeMedico disponibilidadeMedico) {
        Medico medico = disponibilidadeMedico.getMedico();
        int diaSemana = disponibilidadeMedico.getDiaSemana();
        LocalTime horaInicio = disponibilidadeMedico.getHoraInicio();
        LocalTime horaFim = disponibilidadeMedico.getHoraFim();

        return disponibilidadeMedicoRepository
                .verificaSobreposicao(medico, diaSemana, horaInicio, horaFim);
    }

    public void validarDelecao(DisponibilidadeMedico disponibilidadeMedico) {
        Medico medico = disponibilidadeMedico.getMedico();
        int diaSemana = disponibilidadeMedico.getDiaSemana();
        LocalTime horaInicio = disponibilidadeMedico.getHoraInicio();
        LocalTime horaFim = disponibilidadeMedico.getHoraFim();

        if(consultaRepository.existeConsulta(medico, diaSemana, horaInicio, horaFim)) {
            throw new DelecaoNaoPermitidaException("Disponibilidade não pode ser excluída pois há consultas agendadas para o médico neste dia e horário.");
        }
    }
}
