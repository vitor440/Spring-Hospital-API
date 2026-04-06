package com.gerenciamento_hospitalar.file.imports.impl;

import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.file.imports.contract.MedicoImporter;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.model.Especialidade;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class MedicoImporterCsv implements MedicoImporter {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Override
    public List<Medico> importFile(InputStream inputStream) {
        List<Medico> medicos = new ArrayList<>();
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
                Medico medico = new Medico();

                medico.setCrm(record.get("crm"));
                medico.setNome(record.get("nome"));
                medico.setEmail(record.get("email"));
                medico.setTelefone(record.get("telefone"));
                medico.setEspecialidade(Especialidade.valueOf(record.get("especialidade")));
                Long departamentoId = Long.parseLong(record.get("departamento-id"));
                Departamento departamento = departamentoRepository.findById(departamentoId)
                        .orElseThrow(() ->  new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));

                medico.setDepartamento(departamento);
                medicos.add(medico);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return medicos;
    }
}
