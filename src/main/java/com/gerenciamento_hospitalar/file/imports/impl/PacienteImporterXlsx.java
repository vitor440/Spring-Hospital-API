package com.gerenciamento_hospitalar.file.imports.impl;

import com.gerenciamento_hospitalar.file.imports.contract.PacienteImporter;
import com.gerenciamento_hospitalar.model.Paciente;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class PacienteImporterXlsx implements PacienteImporter {


    @Override
    public List<Paciente> importFile(InputStream inputStream) {
        List<Paciente> pacientes = new ArrayList<>();
        try(Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheetAt = workbook.getSheetAt(0);

            Iterator<Row> rows = sheetAt.iterator();

            if (rows.hasNext()) rows.next();

            while(rows.hasNext()) {
                Row row = rows.next();
                Paciente paciente = new Paciente();
                paciente.setCpf(row.getCell(0).getStringCellValue());
                paciente.setNome(row.getCell(1).getStringCellValue());
                paciente.setGenero(row.getCell(2).getStringCellValue());
                paciente.setEndereco(row.getCell(3).getStringCellValue());
                paciente.setEmail(row.getCell(4).getStringCellValue());
                paciente.setTelefone(row.getCell(5).getStringCellValue());
                paciente.setTipoSanguineo(row.getCell(6).getStringCellValue());
                LocalDate dataNascimento = LocalDate.parse(row.getCell(7).getStringCellValue());
                paciente.setDataNascimento(dataNascimento);

                pacientes.add(paciente);
            }
        }
        catch (Exception e) {

        }
        return pacientes;
    }
}
