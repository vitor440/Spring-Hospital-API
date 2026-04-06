package com.gerenciamento_hospitalar.file.imports.impl;

import com.gerenciamento_hospitalar.file.imports.contract.PacienteImporter;
import com.gerenciamento_hospitalar.model.Paciente;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.engine.jdbc.ReaderInputStream;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PacienteImporterCsv implements PacienteImporter {


    @Override
    public List<Paciente> importFile(InputStream inputStream) {
        List<Paciente> pacientes = new ArrayList<>();
        CSVFormat csvFormat = CSVFormat.Builder
                .create()
                .setHeader()
                .setIgnoreHeaderCase(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();

        try {
            Iterable<CSVRecord> records = csvFormat.parse(new InputStreamReader(inputStream));
            for (CSVRecord record : records) {
                Paciente paciente = new Paciente();

                paciente.setCpf(record.get("cpf"));
                paciente.setNome(record.get("nome"));
                paciente.setGenero(record.get("genero"));
                paciente.setEndereco(record.get("endereco"));
                paciente.setEmail(record.get("email"));
                paciente.setTelefone(record.get("telefone"));
                paciente.setTipoSanguineo(record.get("tipo-sanguineo"));
                LocalDate dataNascimento = LocalDate.parse(record.get("data-nascimento"));
                paciente.setDataNascimento(dataNascimento);

                pacientes.add(paciente);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pacientes;
    }
}
