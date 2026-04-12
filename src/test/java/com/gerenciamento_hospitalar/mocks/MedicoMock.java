package com.gerenciamento_hospitalar.mocks;

import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.model.Especialidade;
import com.gerenciamento_hospitalar.model.Medico;

public class MedicoMock {

    public static Medico mockMedico(int i) {
        Medico medico1 = new Medico();
        medico1.setNome("Nome" + i);
        medico1.setCrm("crm-" + i);
        medico1.setEmail("Email" + i);
        medico1.setTelefone("Telefone" + i);
        medico1.setEspecialidade(i % 2 == 0? Especialidade.NEUROLOGISTA: Especialidade.CARDIOLOGISTA);
        medico1.setDepartamento(DepartamentoMock.mockDepartamento(i));
        medico1.setUsuario(UsuarioMock.mockUsuario(1));

        return medico1;
    }

    public static Medico mockMedicoSalvo(int i) {
        Medico medico1 = mockMedico(i);
        medico1.setId(1L);
        return medico1;
    }

    public static MedicoRequest mockRequest(int i) {
        Especialidade neurologista = i % 2 == 0? Especialidade.NEUROLOGISTA: Especialidade.CARDIOLOGISTA;
        String crm = "crm-" + i;
        String nome = "Nome" + i;
        String email = "Email" + i;
        String telefone = "Telefone" + i;

        MedicoRequest request = new MedicoRequest(crm, nome, email, telefone, neurologista, 1L, 1L);
        return request;
    }

    public static MedicoResponse mockResponse(int i) {
        Especialidade neurologista = i % 2 == 0? Especialidade.NEUROLOGISTA: Especialidade.CARDIOLOGISTA;
        String crm = "crm-" + i;
        String nome = "Nome" + i;
        String email = "Email" + i;
        String telefone = "Telefone" + i;

        MedicoResponse request = new MedicoResponse(1L, crm, nome, email, telefone, neurologista, 1L);
        return request;
    }
}
