package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
