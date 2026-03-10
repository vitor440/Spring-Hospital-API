package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Optional<Departamento> findByNome(String nome);

}
