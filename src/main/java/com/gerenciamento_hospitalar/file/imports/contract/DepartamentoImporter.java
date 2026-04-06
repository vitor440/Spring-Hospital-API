package com.gerenciamento_hospitalar.file.imports.contract;

import com.gerenciamento_hospitalar.model.Departamento;

import java.io.InputStream;
import java.util.List;

public interface DepartamentoImporter {

    List<Departamento> importFile(InputStream inputStream);
}
