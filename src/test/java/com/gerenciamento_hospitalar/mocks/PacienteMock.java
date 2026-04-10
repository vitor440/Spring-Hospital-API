package com.gerenciamento_hospitalar.mocks;

import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.model.Paciente;

import java.time.LocalDate;

public class PacienteMock {

    public static Paciente mockPaciente(int i) {
        Paciente paciente = new Paciente();
        paciente.setCpf("449.815.530-05");
        paciente.setNome("paciente1");
        paciente.setEmail("paciente@email.com" + i);
        paciente.setEndereco("endereço" + i);
        paciente.setGenero(i % 2 == 0? "feminino": "masculino");
        paciente.setTelefone("Telefone" + i);
        paciente.setTipoSanguineo(i % 2 == 0? "O+": "O-");
        paciente.setDataNascimento(LocalDate.of(2000 + i, i, i ));
        paciente.setUsuario(null);

        return paciente;
    }

    public static Paciente mockPacienteSalvo(int i) {
        Paciente paciente = mockPaciente(i);
        paciente.setId((long) i);
        return paciente;
    }


    public static PacienteRequest mockPacienteRequest(int i) {
        String cpf = "449.815.530-05";
        String nome = "paciente" + i;
        String email = "paciente@email.com" + i;
        String genero = i % 2 == 0? "feminino": "masculino";
        String telefone = "Telefone" + i;
        String tipoSanguineo = i % 2 == 0? "O+": "O-";
        String endereco = "endereço" + i;
        LocalDate dataNascimento = LocalDate.of(2000 + i, i, i );

        PacienteRequest request = new PacienteRequest(cpf, nome, genero, endereco, email, telefone, tipoSanguineo, dataNascimento, (long) i);

        return request;
    }

    public static PacienteResponse mockPacienteResponse(int i) {
        String cpf = "449.815.530-05";
        String nome = "paciente" + i;
        String email = "paciente@email.com" + i;
        String genero = i % 2 == 0? "feminino": "masculino";
        String telefone = "Telefone" + i;
        String tipoSanguineo = i % 2 == 0? "O+": "O-";
        String endereco = "endereço" + i;
        LocalDate dataNascimento = LocalDate.of(2000 + i, i, i );

        PacienteResponse response = new PacienteResponse((long) i, cpf, nome, genero, endereco, email, telefone, tipoSanguineo, dataNascimento);

        return response;
    }
}
