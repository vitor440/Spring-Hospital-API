package com.gerenciamento_hospitalar.file.imports.impl;

import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.file.imports.contract.MedicoImporter;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.model.Especialidade;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class MedicoImporterXlsx implements MedicoImporter {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Override
    public List<Medico> importFile(InputStream inputStream) {
        List<Medico> medicos = new ArrayList<>();
        try(Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheetAt = workbook.getSheetAt(0);

            Iterator<Row> rows = sheetAt.iterator();

            if (rows.hasNext()) rows.next();

            while(rows.hasNext()) {
                Row row = rows.next();
                Medico medico = new Medico();
                medico.setCrm(row.getCell(0).getStringCellValue());
                medico.setNome(row.getCell(1).getStringCellValue());
                medico.setEmail(row.getCell(2).getStringCellValue());
                medico.setTelefone(row.getCell(3).getStringCellValue());
                medico.setEspecialidade(Especialidade.valueOf(row.getCell(4).getStringCellValue()));
                Long departamentoId = (long) row.getCell(5).getNumericCellValue();
                Departamento departamento = departamentoRepository.findById(departamentoId)
                        .orElseThrow(() ->  new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));

                medico.setDepartamento(departamento);
                medicos.add(medico);
            }
        }
        catch (Exception e) {

        }
        return medicos;
    }
}
