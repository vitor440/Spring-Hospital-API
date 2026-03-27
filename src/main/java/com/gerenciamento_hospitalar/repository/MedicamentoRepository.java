package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
}
