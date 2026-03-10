package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Optional<Consulta> findByMedicoAndDataAndHora(Medico medico, LocalDate data, LocalTime hora);
}
