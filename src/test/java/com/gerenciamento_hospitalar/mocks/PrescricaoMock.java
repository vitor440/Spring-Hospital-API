package com.gerenciamento_hospitalar.mocks;

import com.gerenciamento_hospitalar.dto.request.PrescricaoRequest;
import com.gerenciamento_hospitalar.dto.response.PrescricaoResponse;
import com.gerenciamento_hospitalar.model.Prescricao;

import java.time.LocalDate;
import java.util.List;

public class PrescricaoMock {

    public static Prescricao mockPrescricao(int i) {
        Prescricao prescricao = new Prescricao();
        prescricao.setConsulta(ConsultaMock.mockConsulta(i));
        prescricao.setMedicoId((long) i);
        prescricao.setPacienteId((long) i);
        prescricao.setComentarios("Comentarios" + i);
        prescricao.setDataPrescricao(LocalDate.of(2000 + i, i, i ));

        return prescricao;
    }

    public static PrescricaoRequest mockPrescricaoRequest(int i) {
        return new PrescricaoRequest(LocalDate.of(2000 + i, i, i ),
                "Comentarios" + i, List.of(MedicamentoMock.mockMedicamentoRequest(1)));
    }

    public static PrescricaoResponse mockPrescricaoResponse(int i) {
        return new PrescricaoResponse((long) i, (long) i, (long) i, (long) i, LocalDate.of(2000 + i, i, i ),
                "Comentarios" + i, List.of(MedicamentoMock.mockMedicamentoResponse(1)));
    }
}
