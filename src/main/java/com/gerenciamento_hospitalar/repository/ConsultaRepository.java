package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long>, JpaSpecificationExecutor<Consulta> {

    Optional<Consulta> findByMedicoAndDataAndHora(Medico medico, LocalDate data, LocalTime hora);

    boolean existsByMedico(Medico medico);

    boolean existsByPaciente(Paciente paciente);

    @Query("""
        SELECT count(c) > 0 FROM Consulta c WHERE
        c.medico = :medico 
        AND c.diaSemana = :diaSemana 
        AND c.hora BETWEEN :horaInicio AND :horaFim
    """)
    boolean existeConsulta(Medico medico, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFim);

    @Modifying
    @Transactional
    @Query("UPDATE Consulta c SET c.status = :status WHERE c.id = :id")
    void modificaStatusConsulta(@Param("id") Long id, @Param("status") StatusConsulta status);
}
