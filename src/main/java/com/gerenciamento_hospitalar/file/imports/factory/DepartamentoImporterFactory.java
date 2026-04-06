package com.gerenciamento_hospitalar.file.imports.factory;

import com.gerenciamento_hospitalar.file.imports.contract.DepartamentoImporter;
import com.gerenciamento_hospitalar.file.imports.contract.PacienteImporter;
import com.gerenciamento_hospitalar.file.imports.impl.DepartamentoImporterCsv;
import com.gerenciamento_hospitalar.file.imports.impl.DepartamentoImporterXlsx;
import com.gerenciamento_hospitalar.file.imports.impl.PacienteImporterCsv;
import com.gerenciamento_hospitalar.file.imports.impl.PacienteImporterXlsx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DepartamentoImporterFactory {

    @Autowired
    private ApplicationContext context;

    public DepartamentoImporter getImporter(String filename) {
        if(filename.endsWith(".csv")) {
            return context.getBean(DepartamentoImporterCsv.class);
        }
        else if(filename.endsWith(".xlsx")) {
            return context.getBean(DepartamentoImporterXlsx.class);
        }
        else {
            throw new RuntimeException("Arquivo não suportado para importação!");
        }
    }
}
