package com.gerenciamento_hospitalar.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "prescricao")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Prescricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    @Column(name = "data_prescricao")
    @CreatedDate
    private LocalDate dataPrescricao;

    @Column(name = "comentarios")
    private String comentarios;
}
