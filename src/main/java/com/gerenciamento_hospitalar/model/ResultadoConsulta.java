package com.gerenciamento_hospitalar.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "resultado_consulta")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ResultadoConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medico_id")
    private Long medicoId;

    @Column(name = "paciente_id")
    private Long pacienteId;

    @OneToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescricao_id")
    private Prescricao prescricao;

    @Type(ListArrayType.class)
    @Column(name = "sintomas", columnDefinition = "varchar[]")
    private List<String> sintomas;

    @Column(name = "diagnostico")
    private String diagnostico;

    @Column(name = "notas")
    private String notas;

    @Column(name = "tratamento")
    private String tratamento;

    @Column(name = "data_retorno")
    private LocalDate dataRetorno;

    @Column(name = "data_criaca")
    @CreatedDate
    private LocalDateTime dataCriacao;;
}
