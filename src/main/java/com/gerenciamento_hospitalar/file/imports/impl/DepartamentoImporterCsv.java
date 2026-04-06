package com.gerenciamento_hospitalar.file.imports.impl;

import com.gerenciamento_hospitalar.file.imports.contract.DepartamentoImporter;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.model.Paciente;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartamentoImporterCsv implements DepartamentoImporter {

    @Override
    public List<Departamento> importFile(InputStream inputStream) {
        List<Departamento> departamentos = new ArrayList<>();
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
                Departamento departamento = new Departamento();

                departamento.setNome(record.get("nome"));
                departamento.setLocalizacao(record.get("localizacao"));

                departamentos.add(departamento);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return departamentos;
    }
}
