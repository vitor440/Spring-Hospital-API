package com.gerenciamento_hospitalar.file.imports.impl;

import com.gerenciamento_hospitalar.file.imports.contract.DepartamentoImporter;
import com.gerenciamento_hospitalar.model.Departamento;
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
public class DepartamentoImporterXlsx implements DepartamentoImporter {

    @Override
    public List<Departamento> importFile(InputStream inputStream) {
        List<Departamento> departamentos = new ArrayList<>();
        try(Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheetAt = workbook.getSheetAt(0);

            Iterator<Row> rows = sheetAt.iterator();

            if (rows.hasNext()) rows.next();

            while(rows.hasNext()) {
                Row row = rows.next();
                Departamento departamento = new Departamento();
                departamento.setNome(row.getCell(0).getStringCellValue());
                departamento.setLocalizacao(row.getCell(1).getStringCellValue());


                departamentos.add(departamento);
            }
        }
        catch (Exception e) {

        }
        return departamentos;
    }
}
