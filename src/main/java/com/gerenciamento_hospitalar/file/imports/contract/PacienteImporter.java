package com.gerenciamento_hospitalar.file.imports.contract;

import com.gerenciamento_hospitalar.model.Paciente;

import java.io.InputStream;
import java.util.List;

public interface PacienteImporter {

    List<Paciente> importFile(InputStream inputStream);
}
