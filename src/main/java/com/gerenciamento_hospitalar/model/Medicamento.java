package com.gerenciamento_hospitalar.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medicamento")
@Data
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescricao_id")
    private Prescricao prescricao;

    @Column(name = "nome_medicamento")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "dosagem")
    private String dosagem;

    @Column(name = "frequencia")
    private String frequencia;

    @Column(name = "duracao")
    private String duracao;
}
