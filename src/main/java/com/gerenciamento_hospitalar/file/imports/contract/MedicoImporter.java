package com.gerenciamento_hospitalar.file.imports.contract;

import com.gerenciamento_hospitalar.model.Medico;

import java.io.InputStream;
import java.util.List;

public interface MedicoImporter {

    List<Medico> importFile(InputStream inputStream);
}
