package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
}
