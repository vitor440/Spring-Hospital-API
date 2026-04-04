package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.TurnoAtendimento;
import com.gerenciamento_hospitalar.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;

public interface TurnoAtendimentoRepository extends JpaRepository<TurnoAtendimento, Long>, JpaSpecificationExecutor<TurnoAtendimento> {

//    @Query("SELECT count(dm) > 0 FROM DisponibilidadeMedico dm WHERE dm.medico = :medico AND dm.diaSemana = :diaSemana AND :horario BETWEEN dm.horaInicio AND dm.horaFim")
//    boolean verificaDisponibilidade(Medico medico, int diaSemana, LocalTime horario);

    @Query("""
            SELECT count(t) > 0 FROM TurnoAtendimento t 
            WHERE t.medico = :medico 
            AND t.diaSemana = :diaSemana 
            AND :horario BETWEEN t.horaInicio AND t.horaFim
    """)
    boolean verificaDisponibilidade(Medico medico, int diaSemana, LocalTime horario);

//    @Query("SELECT count(dm) > 0 FROM DisponibilidadeMedico dm where dm.medico = :medico AND dm.diaSemana = :diaSemana AND dm.horaInicio < :horaFim AND dm.horaFim > :horaInicio")
//    boolean verificaSobreposicao(Medico medico, int diaSemana, LocalTime horaInicio, LocalTime horaFim);

    @Query("""
            SELECT count(t) > 0 FROM TurnoAtendimento t 
            where t.medico = :medico 
            AND t.diaSemana = :diaSemana 
            AND t.horaInicio <= :horaFim AND t.horaFim >= :horaInicio
    """)
    boolean verificaSobreposicao(Medico medico, int diaSemana, LocalTime horaInicio, LocalTime horaFim);
}
