package com.gerenciamento_hospitalar.file.imports.factory;

import com.gerenciamento_hospitalar.file.imports.contract.DepartamentoImporter;
import com.gerenciamento_hospitalar.file.imports.contract.MedicoImporter;
import com.gerenciamento_hospitalar.file.imports.impl.DepartamentoImporterCsv;
import com.gerenciamento_hospitalar.file.imports.impl.DepartamentoImporterXlsx;
import com.gerenciamento_hospitalar.file.imports.impl.MedicoImporterCsv;
import com.gerenciamento_hospitalar.file.imports.impl.MedicoImporterXlsx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MedicoImporterFactory {

    @Autowired
    private ApplicationContext context;

    public MedicoImporter getImporter(String filename) {
        if(filename.endsWith(".csv")) {
            return context.getBean(MedicoImporterCsv.class);
        }
        else if(filename.endsWith(".xlsx")) {
            return context.getBean(MedicoImporterXlsx.class);
        }
        else {
            throw new RuntimeException("Arquivo não suportado para importação!");
        }
    }
}
