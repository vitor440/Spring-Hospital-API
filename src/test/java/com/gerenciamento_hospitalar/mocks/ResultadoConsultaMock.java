package com.gerenciamento_hospitalar.mocks;

import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.model.ResultadoConsulta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ResultadoConsultaMock {

    public static ResultadoConsulta mockResultadoConsulta(int i) {
        ResultadoConsulta resultadoConsulta = new ResultadoConsulta();
        resultadoConsulta.setConsulta(ConsultaMock.mockConsulta(i));
        resultadoConsulta.setSintomas(List.of());
        resultadoConsulta.setNotas("Notas" + i);
        resultadoConsulta.setTratamento("Tratamento" + i);
        resultadoConsulta.setDiagnostico("Diagnostico" + i);
        resultadoConsulta.setPrescricao(PrescricaoMock.mockPrescricao(i));
        resultadoConsulta.setDataCriacao(LocalDateTime.of(2000 + i, i, i, i, i));
        resultadoConsulta.setDataRetorno(LocalDate.of(2000 + i, i, i ));
        resultadoConsulta.setMedicoId((long) i);
        resultadoConsulta.setPacienteId((long) i);

        return resultadoConsulta;
    }

    public static ResultadoConsultaRequest mockRequest(int i) {
        return new ResultadoConsultaRequest(List.of(), "Diagnostico" + i,
                "Notas" + i, "Tratamento" + i, LocalDate.of(2000 + i, i, i ),PrescricaoMock.mockPrescricaoRequest(i));
    }

    public static ResultadoConsultaResponse mockResponse(int i) {
        return new ResultadoConsultaResponse((long) i, (long) i, (long) i, (long) i, List.of(), "Diagnostico" + i,
                "Notas" + i, "Tratamento" + i, LocalDate.of(2000 + i, i, i ), LocalDateTime.of(2000 + i, i, i, i, i),
                PrescricaoMock.mockPrescricaoResponse(i));
    }
}
