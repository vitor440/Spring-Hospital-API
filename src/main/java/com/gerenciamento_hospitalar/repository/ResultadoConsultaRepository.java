package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.ResultadoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultadoConsultaRepository extends JpaRepository<ResultadoConsulta, Long> {

    Optional<ResultadoConsulta> findByConsulta(Consulta consulta);
}
