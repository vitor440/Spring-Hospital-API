package com.gerenciamento_hospitalar.mocks;

import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.model.Departamento;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DepartamentoMock {

    public static Departamento mockDepartamento(int i) {
        Departamento departamento1 = new Departamento();
        departamento1.setNome("Cardiologia" + i);
        departamento1.setLocalizacao("quinto andar" + i);

        return departamento1;
    }

    public static Departamento mockDepartamentoSalvo(int i) {
        Departamento departamento1 = mockDepartamento(i);
        departamento1.setId(1L);
        departamento1.setDataCriacao(LocalDateTime.of(2000 + i, i, i, i, i));

        return departamento1;
    }



    public static DepartamentoRequest mockRequest(int i) {
        DepartamentoRequest request1 = new DepartamentoRequest("Cardiologia" + i, "quinto andar" + i);
        return request1;
    }

    public static DepartamentoResponse mockResponse(int i) {
        DepartamentoResponse response1 = new DepartamentoResponse(1L, "Cardiologia" + i, "quinto andar" + i,
                LocalDateTime.of(2000 + i, i, i, i, i));

        return response1;
    }
}
