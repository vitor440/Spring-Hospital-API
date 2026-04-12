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
import java.util.List;
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



    // === BUSCAS POR MÉDICOS ===

    @Query("SELECT c FROM Consulta c WHERE c.medico.id = :id")
    List<Consulta> obterConsultasPeloIdDoMedico(Long id);

    @Query("SELECT c FROM Consulta c WHERE c.medico.id = :id AND c.status = :status")
    List<Consulta> obterConsultasPeloIdDoMedicoEPeloStatus(Long id, StatusConsulta status);

    // === BUSCAS POR PACIENTES ===

    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :id")
    List<Consulta> obterConsultaPeloIdDoPaciente(Long id);


    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :id AND c.status = :status")
    List<Consulta> obterConsultasPeloIdDoPacienteEPeloStatus(Long id, StatusConsulta status);

}
