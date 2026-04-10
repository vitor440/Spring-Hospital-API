package com.gerenciamento_hospitalar.mocks;

import com.gerenciamento_hospitalar.dto.request.ConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.model.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultaMock {

    public static Consulta mockConsulta(int i) {
        Consulta consulta = new Consulta();
        consulta.setMedico(MedicoMock.mockMedico(i));
        consulta.setPaciente(PacienteMock.mockPaciente(i));
        consulta.setData(LocalDate.of(2000 + i, i, i ));
        consulta.setHora(LocalTime.of(i, i));
        consulta.setProposito("proposito" + i);
       // consulta.setStatus(i % 2 == 0? StatusConsulta.AGENDADA: StatusConsulta.CANCELADA);
        //consulta.setDiaSemana(i % 2 == 0? DiaSemana.SEGUNDA: DiaSemana.TERCA);

        return consulta;
    }

    public static Consulta mockConsultaSalva(int i) {
        Consulta consulta = mockConsulta(i);
        consulta.setStatus(i % 2 == 0? StatusConsulta.AGENDADA: StatusConsulta.CANCELADA);
        consulta.setDiaSemana(i % 2 == 0? DiaSemana.SEGUNDA: DiaSemana.TERCA);
        consulta.setId((long) i);
        return consulta;
    }



    public static ConsultaRequest mockConsultaRequest(int i) {
        LocalDate data = LocalDate.of(2000 + i, i, i);
        LocalTime hora = LocalTime.of(i, i);
        String proposito = "proposito" + i;


        return new ConsultaRequest((long) i, (long) i, proposito, data, hora);
    }

    public static ConsultaResponse mockConsultaResponse(int i) {
        LocalDate data = LocalDate.of(2000 + i, i, i);
        LocalTime hora = LocalTime.of(i, i);
        String proposito = "proposito" + i;
        StatusConsulta statusConsulta = i % 2 == 0 ? StatusConsulta.AGENDADA : StatusConsulta.CANCELADA;
        DiaSemana diaSemana = i % 2 == 0 ? DiaSemana.SEGUNDA : DiaSemana.TERCA;


        return new ConsultaResponse((long) i, (long) i, (long) i, proposito, data, hora, diaSemana, statusConsulta);
    }


}
