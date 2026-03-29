package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.Prescricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrescricaoRepository extends JpaRepository<Prescricao, Long> {

}
