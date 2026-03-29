package com.gerenciamento_hospitalar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "consulta")
@ToString(exclude = "resultadoConsulta")
@Data
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(name = "proposito")
    private String proposito;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "dia_semana")
    private Integer diaSemana;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusConsulta status;

    @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL)
    private ResultadoConsulta resultadoConsulta;

    @OneToOne(mappedBy = "consulta")
    private Prescricao prescricao;

}
