package com.gerenciamento_hospitalar.mocks;

import com.gerenciamento_hospitalar.dto.request.MedicamentoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicamentoResponse;
import com.gerenciamento_hospitalar.model.Medicamento;

public class MedicamentoMock {

    public static Medicamento mockMedicamento(int i) {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome("Nome" + i);
        medicamento.setDescricao("Descrição" + i);
        medicamento.setTipo("Tipo" + i);
        medicamento.setDosagem("Dosagem" + i);
        medicamento.setDuracao("Duração" + i);
        medicamento.setFrequencia("Frequência" + i);
        medicamento.setPrescricao(PrescricaoMock.mockPrescricao(i));

        return medicamento;
    }

    public static MedicamentoRequest mockMedicamentoRequest(int i) {
        return new MedicamentoRequest("Nome" + i, "Tipo" + i,
                "Descrição" + i, "Dosagem" + i, "Frequência" + i, "Duração" + i);
    }

    public static MedicamentoResponse mockMedicamentoResponse(int i) {
        return new MedicamentoResponse((long) i, (long) i, "Nome" + i, "Tipo" + i,
                "Descrição" + i, "Dosagem" + i, "Frequência" + i, "Duração" + i);
    }
}
