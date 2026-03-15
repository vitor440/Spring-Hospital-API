package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long>, JpaSpecificationExecutor<Consulta> {

    Optional<Consulta> findByMedicoAndDataAndHora(Medico medico, LocalDate data, LocalTime hora);

    boolean existsByMedico(Medico medico);

    boolean existsByPaciente(Paciente paciente);

    boolean existsByMedicoAndHoraBetween(Medico medico, LocalTime horaInicio, LocalTime horaFim);
}
