package com.gerenciamento_hospitalar.file.imports.factory;

import com.gerenciamento_hospitalar.file.imports.contract.PacienteImporter;
import com.gerenciamento_hospitalar.file.imports.impl.PacienteImporterCsv;
import com.gerenciamento_hospitalar.file.imports.impl.PacienteImporterXlsx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class PacienteImporterFactory {

    @Autowired
    private ApplicationContext context;

    public PacienteImporter getImporter(String filename) {
        if(filename.endsWith(".csv")) {
            return context.getBean(PacienteImporterCsv.class);
        }
        else if(filename.endsWith(".xlsx")) {
            return context.getBean(PacienteImporterXlsx.class);
        }
        else {
            throw new RuntimeException("Arquivo não suportado para importação!");
        }
    }
}
