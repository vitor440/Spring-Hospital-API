package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.DisponibilidadeMedico;
import com.gerenciamento_hospitalar.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;

public interface DisponibilidadeMedicoRepository extends JpaRepository<DisponibilidadeMedico, Long>, JpaSpecificationExecutor<DisponibilidadeMedico> {

//    @Query("SELECT TRUE FROM DisponibilidadeMedico dm WHERE dm.medico = :medico AND dm.diaSemana = :diaSemana AND :horario BETWEEN dm.horaInicio AND dm.horaFim")
//    boolean verificaDisponibilidade(Medico medico, int diaSemana, LocalTime horario);
}
