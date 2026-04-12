package com.gerenciamento_hospitalar.mocks;

import com.gerenciamento_hospitalar.dto.request.TurnoAtendimentoRequest;
import com.gerenciamento_hospitalar.dto.response.TurnoAtendimentoResponse;
import com.gerenciamento_hospitalar.model.DiaSemana;
import com.gerenciamento_hospitalar.model.TurnoAtendimento;

import java.time.LocalTime;

public class TurnoMock {

    public static TurnoAtendimento mockTurno(int i) {
        TurnoAtendimento turnoAtendimento = new TurnoAtendimento();
        turnoAtendimento.setMedico(MedicoMock.mockMedico(1));
        turnoAtendimento.setHoraInicio(LocalTime.of(i, i));
        turnoAtendimento.setHoraFim(LocalTime.of(i, i));
        turnoAtendimento.setDiaSemana(i % 2 == 0? DiaSemana.SEGUNDA: DiaSemana.TERCA);

        return turnoAtendimento;
    }

    public static TurnoAtendimentoRequest mockTurnoRequest(int i) {
        return new TurnoAtendimentoRequest(i % 2 == 0? DiaSemana.SEGUNDA: DiaSemana.TERCA, LocalTime.of(i, i), LocalTime.of(i, i));
    }

    public static TurnoAtendimentoResponse mockTurnoResponse(int i) {
        return new TurnoAtendimentoResponse(1L, 1L, i % 2 == 0? DiaSemana.SEGUNDA: DiaSemana.TERCA,
                LocalTime.of(i, i), LocalTime.of(i, i));
    }

}
