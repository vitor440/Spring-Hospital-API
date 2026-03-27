package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.PrescricaoMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescricaoMedicamentoRepository extends JpaRepository<PrescricaoMedicamento, Long> {
}
